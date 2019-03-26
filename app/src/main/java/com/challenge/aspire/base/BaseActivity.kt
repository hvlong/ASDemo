package com.challenge.aspire.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.challenge.aspire.App
import com.challenge.aspire.data.source.remote.api.error.RetrofitException
import com.challenge.aspire.utils.LogUtils
import com.challenge.aspire.utils.extension.notNull
import com.challenge.aspire.utils.widget.dialog.DialogAlert
import com.challenge.aspire.utils.widget.dialog.DialogConfirm
import com.challenge.aspire.utils.widget.dialog.DialogManager
import com.challenge.aspire.utils.widget.dialog.DialogManagerImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author LongHV.
 */

abstract class BaseActivity : AppCompatActivity(), BaseView {

    val compositeDisposable = CompositeDisposable()

    var dialogManager: DialogManager? = null

    override fun onStart() {
        super.onStart()
        App.instance.currentClass = this::class.java
        LogUtils.d("BaseActivity: ", this::class.java.simpleName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogManager = DialogManagerImpl(this)
        initDagger()
        initView(savedInstanceState)
        initPresenter()
        initialize()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        dialogManager?.onRelease()
        super.onDestroy()
    }

    override fun showLoading() {
        dialogManager?.showLoading()
    }

    override fun hideLoading() {
        dialogManager?.hideLoading()
    }

    override fun showToastSuccess(message: String) {
        dialogManager?.showToastSuccess(message)
    }

    override fun showDialogType(toastType: String, message: String) {
        dialogManager?.showDialogType(toastType, message)
    }

    override fun showAlertDialog(title: String, message: String,
            titleButton: String, listener: DialogAlert.Companion.OnButtonClickedListener?) {
        dialogManager?.showAlertDialog(title, message, titleButton, listener)
    }

    override fun showConfirmDialog(title: String?, message: String?,
            titleButtonPositive: String, titleButtonNegative: String,
            listener: DialogConfirm.OnButtonClickedListener?) {
        dialogManager?.showConfirmDialog(title, message, titleButtonPositive, titleButtonNegative,
                listener)
    }

    override fun handleApiError(apiError: Throwable) {
        if (apiError is RetrofitException) {
            apiError.getMsgError().notNull {
                showAlertDialog(message = it)
            }
        }
    }

    override fun onError(@StringRes resId: Int) {
    }

    override fun launchDisposable(job: () -> Disposable) {
        compositeDisposable?.add(job())
    }

    protected abstract fun initDagger()
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun initPresenter()
    protected abstract fun initialize()
}
