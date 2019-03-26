package com.challenge.aspire.data.source.remote.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
        @Expose
        @SerializedName("name")
        var name: String = "",
        @Expose
        @SerializedName("email")
        var email: String = "",
        @Expose
        @SerializedName("password")
        var password: String = "",
        @Expose
        @SerializedName("password_confirmation")
        var confirmPassword: String = ""
)