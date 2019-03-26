package com.challenge.aspire.screen.verifyAuth

import com.challenge.aspire.base.BasePresenter
import com.challenge.aspire.base.BaseView
import com.challenge.aspire.data.model.Image
import com.challenge.aspire.data.model.User
import java.io.File

class VerifyAuthContract {
    interface View : BaseView {

        fun onVerifyAuthSuccess()

    }

    interface Presenter : BasePresenter<View> {

        fun verifyAuth(identity: File, selfieList: MutableList<Image>)

    }
}
