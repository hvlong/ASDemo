package com.challenge.aspire.screen.profile

import com.challenge.aspire.base.BasePresenter
import com.challenge.aspire.base.BaseView
import com.challenge.aspire.data.model.User
import java.io.File

class ProfileContract {
    interface View : BaseView {

        fun onGetUserProfileSuccess(response: User?)

        fun onAttachBankStatementSuccess()

    }

    interface Presenter : BasePresenter<View> {

        fun getUserProfile(userId: Int)

        fun attachBankStatement(file: File)

    }
}
