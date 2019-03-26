package com.challenge.aspire.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.challenge.aspire.R
import com.challenge.aspire.utils.extension.notNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseDialogFragment : DialogFragment() {

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dialog)
        this.initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return getLayout()
    }

    override fun onStart() {
        super.onStart()
        val dlg = dialog
        dlg.notNull {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dlg?.window?.setLayout(width, height)
        }
    }

    override fun onDetach() {
        compositeDisposable.clear()
        super.onDetach()
    }

    fun launchDisposable(job: () -> Disposable) {
        compositeDisposable.add(job())
    }

    abstract fun getLayout(): View
    abstract fun initData()
}