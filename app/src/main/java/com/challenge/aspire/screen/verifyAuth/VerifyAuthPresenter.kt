package com.challenge.aspire.screen.verifyAuth

import com.challenge.aspire.data.model.Image
import com.challenge.aspire.data.source.remote.repositorys.UserRepository
import com.challenge.aspire.utils.extension.notNull
import com.challenge.aspire.utils.extension.withScheduler
import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VerifyAuthPresenter
@Inject constructor(
        private val userRepository: UserRepository,
        private val baseScheduleProvider: BaseScheduleProvider) : VerifyAuthContract.Presenter {

    private var view: VerifyAuthContract.View? = null

    override fun onAttach(view: VerifyAuthContract.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun verifyAuth(identity: File, selfieList: MutableList<Image>) {
        val identityRequest = RequestBody.create(MediaType.parse("image/*"), identity)
        val identityBody = MultipartBody.Part.createFormData("identity", identity.name,
                identityRequest)
        val surveyImagesParts: MutableList<MultipartBody.Part> = mutableListOf()
        for (image in selfieList) {
            image.file.notNull {
                val surveyBody = RequestBody.create(MediaType.parse("image/*"), it)
                surveyImagesParts.add(MultipartBody.Part.createFormData("selFies", identity.name, surveyBody))
            }
        }
        view?.launchDisposable {
            userRepository.verifyAuth(identityBody, surveyImagesParts)
                    .delay(5, TimeUnit.SECONDS)
                    .doOnSubscribe { view?.showLoading() }
                    .doAfterTerminate { view?.hideLoading() }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onSuccess = {
                                view?.onVerifyAuthSuccess()
                            },
                            onError = {
                                view?.handleApiError(it)
                            }
                    )

        }
    }

}