package com.challenge.aspire.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.challenge.aspire.utils.widget.dialog.DialogAlert
import com.challenge.aspire.utils.widget.dialog.DialogConfirm
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author LongHV.
 */

abstract class BaseFragment : Fragment(), BaseView {

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return initView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
        initialize()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun showLoading() {
        if (activity is BaseActivity) (activity as BaseActivity).showLoading()
    }

    override fun hideLoading() {
        if (activity is BaseActivity) (activity as BaseActivity).hideLoading()
    }

    override fun handleApiError(apiError: Throwable) {
        if (activity is BaseActivity) (activity as BaseActivity).handleApiError(apiError)
    }

    override fun showToastSuccess(message: String) {
        if (activity is BaseActivity) (activity as BaseActivity).showToastSuccess(message)
    }

    override fun showDialogType(toastType: String, message: String) {
        if (activity is BaseActivity) (activity as BaseActivity).showDialogType(toastType, message)
    }

    override fun showAlertDialog(title: String, message: String,
            titleButton: String, listener: DialogAlert.Companion.OnButtonClickedListener?) {
        if (activity is BaseActivity) (activity as BaseActivity).showAlertDialog(title,
                message, titleButton, listener)
    }

    override fun showConfirmDialog(title: String?, message: String?,
            titleButtonPositive: String, titleButtonNegative: String,
            listener: DialogConfirm.OnButtonClickedListener?) {
        if (activity is BaseActivity) (activity as BaseActivity).showConfirmDialog(title,
                message, titleButtonPositive, titleButtonNegative, listener)
    }

    override fun onError(@StringRes resId: Int) {
        if (activity is BaseActivity) (activity as BaseActivity).onError(resId)
    }

    override fun launchDisposable(job: () -> Disposable) {
        compositeDisposable.add(job())
    }

    protected abstract fun initDagger()
    protected abstract fun initView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View

    protected abstract fun initPresenter()
    protected abstract fun initialize()
}
