package com.challenge.aspire.data.source.remote.repositorys

import com.challenge.aspire.data.model.User
import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsApi
import com.challenge.aspire.data.source.local.sharedprf.SharedPrefsKey
import com.challenge.aspire.data.source.remote.api.request.RegisterRequest
import com.challenge.aspire.data.source.remote.network.ApiService
import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Part
import javax.inject.Inject

interface UserRepository {

    fun saveUserLocal(user: User)

    fun doRegisterUser(registerRequest: RegisterRequest?): Single<Boolean>

    fun getUserProfile(userId: Int): Single<User>

    fun attachBankStatement(body: MultipartBody.Part): Single<Boolean>

    fun verifyAuth(identity: MultipartBody.Part, selfieFileList: MutableList<MultipartBody.Part>): Single<Boolean>
}

class UserRepositoryImpl
@Inject constructor(
        private val gson: Gson,
        private val sharedPrefsApi: SharedPrefsApi,
        private val apiService: ApiService) : UserRepository {

    override fun saveUserLocal(user: User) {
        val data = gson.toJson(user, User::class.java)
        sharedPrefsApi.put(SharedPrefsKey.KEY_USER, data)
    }

    override fun doRegisterUser(registerRequest: RegisterRequest?): Single<Boolean> {
//        return apiService.registerUser(registerRequest).map { it.success }
        return Single.just(true)
    }

    override fun getUserProfile(userId: Int): Single<User> {
        val user = User()
        user.id = userId
        user.name = "Hoang Long"
        user.email = "hvlong.it@gmail.com"
        user.address = "Da Nang, Viet Nam"
//        return apiService.getUserProfile()
        return Single.just(user)
    }

    override fun attachBankStatement(file: MultipartBody.Part): Single<Boolean> {
//        return apiService.attachBankStatement(fileIdentity)
        return Single.just(true)
    }

    override fun verifyAuth(identity: MultipartBody.Part,
            selfieFileList: MutableList<MultipartBody.Part>): Single<Boolean> {
//        return apiService.verifyAuth(identity, selfieFileList)
        return Single.just(true)
    }

}