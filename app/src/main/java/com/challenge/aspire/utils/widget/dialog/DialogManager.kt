package com.challenge.aspire.utils.widget.dialog

import com.challenge.aspire.utils.ToastType

interface DialogManager {

    fun showLoading()

    fun showProcessing()

    fun hideLoading()

    fun onRelease()

    fun showDialogType(@ToastType.Toast toastType: String, message: String)

    fun showToastSuccess(message: String)

    fun showAlertDialog(title: String, message: String, titleButton: String,
            listener: DialogAlert.Companion.OnButtonClickedListener?)

    fun showConfirmDialog(title: String?, message: String?,
            titleButtonPositive: String, titleButtonNegative: String,
            listener: DialogConfirm.OnButtonClickedListener?)

}
