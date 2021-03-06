package com.challenge.aspire.data.source.remote.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
        @Expose
        @SerializedName("status")
        var status: Boolean? = null,
        @Expose
        @SerializedName("total")
        var total: Int = 0,
        @Expose
        @SerializedName("data")
        var data: T? = null,
        @Expose
        @SerializedName("success")
        val success: Boolean = false
)
