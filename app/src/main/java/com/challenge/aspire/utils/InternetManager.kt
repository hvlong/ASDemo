package com.challenge.aspire.utils

import android.content.Context
import android.net.ConnectivityManager
import com.challenge.aspire.App


class InternetManager {

    companion object {
        fun isConnected(): Boolean {
            val cm = App.instance.applicationContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm?.activeNetworkInfo != null
        }
    }

}