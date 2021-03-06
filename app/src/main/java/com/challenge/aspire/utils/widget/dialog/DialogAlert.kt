package com.challenge.aspire.utils.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.challenge.aspire.R
import com.challenge.aspire.utils.RxView
import com.challenge.aspire.utils.StringUtils
import com.challenge.aspire.utils.extension.notNull
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_alert.*

class DialogAlert : DialogFragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    var listener: OnButtonClickedListener? = null
    private var title: String? = ""
    private var message: String? = ""
    private var titleBtn: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        compositeDisposable = CompositeDisposable()

        arguments?.let {
            title = it.getString(
                    TITLE_EXTRA)
            message = it.getString(
                    MESSAGE_EXTRA)
            titleBtn = it.getString(
                    TITLE_BUTTON_EXTRA)
        }

        return inflater.inflate(R.layout.dialog_alert, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tvTitle.text = title
        tvContent.text = message
        if (StringUtils.isNotEmpty(titleBtn)) {
            btnPositive.text = titleBtn
        }
        if (StringUtils.isBlank(title)) {
            tvTitle.visibility = View.GONE
        }

        val actionDisposable = RxView.clicks(btnPositive,
                false)
                .subscribe {
                    dismiss()
                    listener.notNull { action ->
                        action.onPositiveClicked()
                    }
                }
        compositeDisposable.add(actionDisposable)
    }

    override fun onDestroy() {
        this.compositeDisposable.clear()
        super.onDestroy()
    }

    companion object {
        private const val TITLE_EXTRA = "TITLE_EXTRA"
        private const val MESSAGE_EXTRA = "MESSAGE_EXTRA"
        private const val TITLE_BUTTON_EXTRA = "TITLE_BUTTON_EXTRA"

        fun newInstance(title: String, message: String, titleBtn: String,
                listener: OnButtonClickedListener?): DialogAlert {
            return DialogAlert().apply {
                arguments = Bundle().apply {
                    putString(
                            TITLE_EXTRA, title)
                    putString(
                            MESSAGE_EXTRA, message)
                    putString(
                            TITLE_BUTTON_EXTRA, titleBtn)
                }
                this.listener = listener
            }
        }

        interface OnButtonClickedListener {
            fun onPositiveClicked()
        }
    }
}