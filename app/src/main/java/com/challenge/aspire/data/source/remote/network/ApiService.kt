package com.challenge.aspire.data.source.remote.network

import com.challenge.aspire.data.model.User
import com.challenge.aspire.data.source.remote.api.request.RegisterRequest
import com.challenge.aspire.data.source.remote.api.response.RegisterResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest?): Single<RegisterResponse>

    @GET("profile")
    fun getUserProfile(): Single<User>

    @Multipart
    @POST("/attachment")
    fun attachBankStatement(@Part file: MultipartBody.Part): Single<Boolean>

    @Multipart
    @POST("/verify-auth")
    fun verifyAuth(@Part identity: MultipartBody.Part, @Part selfieFile: MutableList<MultipartBody.Part>): Single<Boolean>
}
