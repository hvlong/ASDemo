package com.challenge.aspire.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        @Expose
        @SerializedName("id")
        var id: Int = 0,
        @Expose
        @SerializedName("name")
        var name: String? = null,
        @Expose
        @SerializedName("email")
        var email: String? = null,
        @Expose
        @SerializedName("address")
        var address: String? = null
) : Parcelable