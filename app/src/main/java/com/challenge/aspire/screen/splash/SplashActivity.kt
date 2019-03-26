package com.challenge.aspire.screen.splash

import android.os.Bundle
import android.os.Handler
import com.challenge.aspire.App
import com.challenge.aspire.R
import com.challenge.aspire.base.BaseActivity
import com.challenge.aspire.screen.register.RegisterActivity
import com.challenge.aspire.utils.extension.startActivityAtRoot
import javax.inject.Inject

/**
 * ---------------------------
 * Created by [Pika] on 03/24/19
 * Screen Name: SplashActivity
 * ---------------------------
 */
class SplashActivity : BaseActivity(), SplashContract.View {

    @Inject
    lateinit var presenter: SplashPresenter

    private var delayHandle: Handler = Handler()

    private val runnable = Runnable {
        startActivityAtRoot(this, RegisterActivity::class.java, args = intent.extras)
        finish()
    }

    override fun initDagger() = (applicationContext as App).appComponent.inject(this)

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_splash)
    }

    override fun initialize() {
        delayHandle.postDelayed(runnable, DELAY_TIME)
    }

    override fun initPresenter() = presenter.onAttach(this)

    override fun onDestroy() {
        presenter.onDetach()
        delayHandle.removeCallbacks(runnable)
        super.onDestroy()
    }

    companion object {
        private const val DELAY_TIME: Long = 500
    }
}