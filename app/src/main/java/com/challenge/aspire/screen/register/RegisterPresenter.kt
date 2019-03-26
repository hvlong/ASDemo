package com.challenge.aspire.screen.register

import com.challenge.aspire.data.source.remote.api.request.RegisterRequest
import com.challenge.aspire.data.source.remote.repositorys.TokenRepository
import com.challenge.aspire.data.source.remote.repositorys.UserRepository
import com.challenge.aspire.utils.extension.withScheduler
import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RegisterPresenter
@Inject constructor(
        private val userRepository: UserRepository,
        private val tokenRepository: TokenRepository,
        private val baseScheduleProvider: BaseScheduleProvider) : RegisterContract.Presenter {

    private var view: RegisterContract.View? = null

    override fun onAttach(view: RegisterContract.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun doRegisterUser(registerRequest: RegisterRequest?) {
        view?.launchDisposable {
            userRepository.doRegisterUser(registerRequest)
                    .delay(5, TimeUnit.SECONDS)
                    .doOnSubscribe { view?.showLoading() }
                    .doAfterTerminate {
                        view?.hideLoading()
                    }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onSuccess = { isSuccess -> if (isSuccess) view?.onRegisterSuccess() },
                            onError = { view?.handleApiError(it) }
                    )
        }
    }

    override fun validateRegisterRequest(registerRequest: RegisterRequest?): Boolean {
        return !registerRequest?.name.isNullOrEmpty() ||
                !registerRequest?.email.isNullOrEmpty() ||
                !registerRequest?.password.isNullOrEmpty() ||
                !registerRequest?.confirmPassword.isNullOrEmpty()
    }


}