package com.challenge.aspire.screen.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.challenge.aspire.App
import com.challenge.aspire.R
import com.challenge.aspire.base.BaseActivity

class HomeActivity : BaseActivity(){

    override fun initPresenter() {
    }

    override fun initDagger() {
        (applicationContext as App).appComponent.inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_home)
    }

    override fun initialize() {

    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
