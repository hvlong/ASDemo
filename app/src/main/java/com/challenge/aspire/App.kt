package com.challenge.aspire

import androidx.appcompat.app.AppCompatDelegate
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.challenge.aspire.utils.Foreground
import com.challenge.aspire.utils.GlideApp
import com.challenge.aspire.utils.LogUtils
import com.challenge.aspire.utils.di.component.DaggerMainComponent
import com.challenge.aspire.utils.di.component.MainComponent
import com.challenge.aspire.utils.di.module.AppModule
import com.challenge.aspire.utils.di.module.NetworkModule
import com.challenge.aspire.utils.di.module.RepositoryModule
import com.squareup.leakcanary.LeakCanary

/**
 * @author LongHV.
 */
class App : SplitCompatApplication(), Foreground.Listener {

    lateinit var appComponent: MainComponent

    var currentClass: Class<*>? = null

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        instance = this

        Foreground.inject(this).addListener(this)

        appComponent = DaggerMainComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule())
                .repositoryModule(RepositoryModule())
                .build()
        appComponent.inject(this)

//        configLeakCanary()

    }

    override fun onLowMemory() {
        GlideApp.get(this).onLowMemory()
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        GlideApp.get(this).onTrimMemory(level)
        super.onTrimMemory(level)
    }

    override fun onBecameForeground() {
        LogUtils.d(TAG, "onBecameForeground")
    }

    override fun onBecameBackground() {
        LogUtils.d(TAG, "onBecameBackground")
    }

    private fun configLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return
            }
            LeakCanary.install(this)
        }
    }

    companion object {

        private const val TAG = "MainApplication"
        lateinit var instance: App
    }

}
