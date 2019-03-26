package com.challenge.aspire.screen.profile

import com.challenge.aspire.data.source.remote.repositorys.UserRepository
import com.challenge.aspire.utils.extension.withScheduler
import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfilePresenter
@Inject constructor(
        private val userRepository: UserRepository,
        private val baseScheduleProvider: BaseScheduleProvider) : ProfileContract.Presenter {

    private var view: ProfileContract.View? = null

    override fun onAttach(view: ProfileContract.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun getUserProfile(userId: Int) {
        view?.launchDisposable {
            userRepository.getUserProfile(userId)
                    .delay(2, TimeUnit.SECONDS)
                    .doOnSubscribe { view?.showLoading() }
                    .doAfterTerminate {
                        view?.hideLoading()
                    }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onSuccess = {
                                view?.onGetUserProfileSuccess(it)
                            },
                            onError = {
                                view?.handleApiError(it)
                            }
                    )
        }
    }

    override fun attachBankStatement(file: File) {
        val fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("pdfFile", file.getName(),
                fileRequest)

        view?.launchDisposable {
            userRepository.attachBankStatement(body)
                    .delay(2, TimeUnit.SECONDS)
                    .doOnSubscribe { view?.showLoading() }
                    .doAfterTerminate { view?.hideLoading() }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onSuccess = {
                                view?.onAttachBankStatementSuccess()
                            },
                            onError = {
                                view?.handleApiError(it)
                            }
                    )

        }
    }

}