package com.challenge.aspire.utils.di.module

import android.app.Application
import android.content.res.Resources
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsApi
import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsImpl
import com.challenge.aspire.data.source.remote.api.middleware.BooleanAdapter
import com.challenge.aspire.data.source.remote.api.middleware.DoubleAdapter
import com.challenge.aspire.data.source.remote.api.middleware.IntegerAdapter
import com.challenge.aspire.data.source.remote.repositorys.TokenRepository
import com.challenge.aspire.data.source.remote.repositorys.TokenRepositoryImpl
import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import com.challenge.aspire.utils.rxAndroid.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author LongHV
 */
@Module
class AppModule(private val mApplication: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @Singleton
    fun provideResources(app: Application): Resources {
        return app.resources
    }

    @Singleton
    @Provides
    fun provideBaseSchedulerProvider(): BaseScheduleProvider {
        return SchedulerProvider()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val booleanAdapter = BooleanAdapter()
        val integerAdapter = IntegerAdapter()
        val doubleAdapter = DoubleAdapter()
        return GsonBuilder()
                .registerTypeAdapter(Boolean::class.java, booleanAdapter)
                .registerTypeAdapter(Int::class.java, integerAdapter)
                .registerTypeAdapter(Double::class.java, doubleAdapter)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation()
                .create()
    }

    @Provides
    @Singleton
    fun provideSharedPrefsApi(app: Application): SharedPrefsApi {
        return SharedPrefsImpl(app)
    }

    @Singleton
    @Provides
    fun provideTokenRepository(app: Application): TokenRepository {
        return TokenRepositoryImpl(SharedPrefsImpl(app))
    }

}
