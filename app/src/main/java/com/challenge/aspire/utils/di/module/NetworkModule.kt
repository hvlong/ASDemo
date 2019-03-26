package com.challenge.aspire.utils.di.module

import android.app.Application
import com.google.gson.Gson
import com.challenge.aspire.BuildConfig
import com.challenge.aspire.data.source.remote.api.middleware.InterceptorImpl
import com.challenge.aspire.data.source.remote.api.middleware.RxErrorHandlingCallAdapterFactory
import com.challenge.aspire.data.source.remote.network.ApiService
import com.challenge.aspire.data.source.remote.repositorys.TokenRepository
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author LongHV.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize: Long = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Singleton
    @Provides
    fun provideInterceptor(tokenRepository: TokenRepository): Interceptor {
        return InterceptorImpl(tokenRepository)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.cache(cache)
        httpClientBuilder.addInterceptor(interceptor)

        httpClientBuilder.readTimeout(
                READ_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(
                WRITE_TIMEOUT, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(
                CONNECTION_TIMEOUT, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            httpClientBuilder.addInterceptor(logging)
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideNetworkService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    companion object {
        private const val READ_TIMEOUT: Long = 30
        private const val WRITE_TIMEOUT: Long = 30
        private const val CONNECTION_TIMEOUT: Long = 30
    }
}
