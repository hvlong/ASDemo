package com.challenge.aspire.utils.di.module

import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsApi
import com.challenge.aspire.data.source.remote.network.ApiService
import com.challenge.aspire.data.source.remote.repositorys.UserRepository
import com.challenge.aspire.data.source.remote.repositorys.UserRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providerUserRepository(gson: Gson, sharedPrefsApi: SharedPrefsApi,
            apiService: ApiService): UserRepository {
        return UserRepositoryImpl(gson, sharedPrefsApi, apiService)
    }
}
