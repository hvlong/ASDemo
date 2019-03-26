package com.challenge.aspire.data.source.remote.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse
(
        @Expose
        @SerializedName("success")
        val success: Boolean = false
)