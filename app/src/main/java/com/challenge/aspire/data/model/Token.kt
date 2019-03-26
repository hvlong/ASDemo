package com.challenge.aspire.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Token(
        @Expose
        @SerializedName("token_type")
        var tokenType: String? = "",
        @Expose
        @SerializedName("access_token")
        var accessToken: String? = ""
)