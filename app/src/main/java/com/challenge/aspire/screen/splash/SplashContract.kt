package com.challenge.aspire.screen.splash

import com.challenge.aspire.base.BasePresenter
import com.challenge.aspire.base.BaseView

interface SplashContract {

    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}
