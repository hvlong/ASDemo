package com.challenge.aspire.screen.splash

import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import javax.inject.Inject

class SplashPresenter @Inject
constructor(
        private val baseScheduleProvider: BaseScheduleProvider) : SplashContract.Presenter {

    private var view: SplashContract.View? = null

    override fun onAttach(view: SplashContract.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

}
