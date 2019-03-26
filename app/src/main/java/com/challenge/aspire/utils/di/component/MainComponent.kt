package com.challenge.aspire.utils.di.component

import com.challenge.aspire.App
import com.challenge.aspire.screen.home.HomeActivity
import com.challenge.aspire.screen.profile.ProfileActivity
import com.challenge.aspire.screen.splash.SplashActivity
import com.challenge.aspire.utils.di.module.AppModule
import com.challenge.aspire.utils.di.module.NetworkModule
import com.challenge.aspire.utils.di.module.RepositoryModule
import com.challenge.aspire.screen.register.RegisterActivity
import com.challenge.aspire.screen.verifyAuth.VerifyAuthActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author LongHV.
 */
@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class])
interface MainComponent {

    fun inject(app: App)

    fun inject(splashActivity: SplashActivity)

    fun inject(homeActivity: HomeActivity)

    fun inject(registerActivity: RegisterActivity)

    fun inject(profileActivity: ProfileActivity)

    fun inject(verifyAuthActivity: VerifyAuthActivity)

}
