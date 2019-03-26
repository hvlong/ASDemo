package com.challenge.aspire.utils

import androidx.annotation.StringDef

interface ToastType {
    @StringDef(ToastType.SUCCESS, ToastType.ERROR, ToastType.INFO, ToastType.WARNING)
    annotation class Toast

    companion object {
        const val SUCCESS = "SUCCESS"
        const val ERROR = "ERROR"
        const val INFO = "INFO"
        const val WARNING = "WARNING"
    }
}