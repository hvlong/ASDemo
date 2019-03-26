package com.challenge.aspire.screen.register

import com.challenge.aspire.base.BasePresenter
import com.challenge.aspire.base.BaseView
import com.challenge.aspire.data.source.remote.api.request.RegisterRequest

class RegisterContract {
    interface View : BaseView {

        fun onRegisterSuccess()

    }

    interface Presenter : BasePresenter<View> {

        fun doRegisterUser(registerRequest: RegisterRequest?)

        fun validateRegisterRequest(registerRequest: RegisterRequest?): Boolean

    }
}
