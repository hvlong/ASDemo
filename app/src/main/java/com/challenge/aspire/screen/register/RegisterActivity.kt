package com.challenge.aspire.screen.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.challenge.aspire.App
import com.challenge.aspire.R
import com.challenge.aspire.base.BaseActivity
import com.challenge.aspire.data.source.remote.api.request.RegisterRequest
import com.challenge.aspire.databinding.ActivityRegisterBinding
import com.challenge.aspire.screen.profile.ProfileActivity
import com.challenge.aspire.utils.LogUtils
import com.challenge.aspire.utils.RxView
import com.challenge.aspire.utils.ValidateUtils
import com.challenge.aspire.utils.extension.notNull
import com.challenge.aspire.utils.extension.withScheduler
import com.challenge.aspire.utils.rxAndroid.BaseScheduleProvider
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : BaseActivity(), RegisterContract.View {

    @Inject
    lateinit var presenter: RegisterPresenter
    @Inject
    lateinit var baseScheduleProvider: BaseScheduleProvider

    var registerRequest = ObservableField<RegisterRequest>(RegisterRequest())
    var nameError = ObservableField<String>("")
    var emailError = ObservableField<String>("")
    var passwordError = ObservableField<String>("")
    var confirmPasswordError = ObservableField<String>("")

    override fun initDagger() = (applicationContext as App).appComponent.inject(this)

    override fun initView(savedInstanceState: Bundle?) {
        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this,
                R.layout.activity_register)
        binding.viewModel = this
    }

    override fun initPresenter() = presenter.onAttach(this)

    override fun initialize() {
        validateInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onRegisterSuccess() {
        startActivity(ProfileActivity.getIntent(this))
        finish()
    }

    fun onRegisterClick() {
        registerRequest.get().notNull {
            if (it.name.isBlank()) {
                nameError.set(getString(R.string.err_msg_name_empty))
            }
            if (it.email.isBlank()) {
                emailError.set(getString(R.string.err_msg_email_empty))
            }
            if (it.password.isBlank()) {
                passwordError.set(getString(R.string.err_msg_password_empty))
            }
            if (it.confirmPassword.isBlank()) {
                confirmPasswordError.set(getString(R.string.err_msg_confirm_password_empty))
            }
        }
        if (!presenter.validateRegisterRequest(registerRequest = registerRequest.get())) {
            return
        }
        if (!nameError.get().isNullOrEmpty()
                || !emailError.get().isNullOrEmpty()
                || !passwordError.get().isNullOrEmpty()
                || !confirmPasswordError.get().isNullOrEmpty()) {
            return
        }
        presenter.doRegisterUser(registerRequest.get())
    }

    private fun validateInfo() {
        launchDisposable {
            RxView.input(edtName)
                    .map {
                        return@map ValidateUtils.validateName(it, applicationContext)
                    }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onNext = { errorMsg -> nameError.set(errorMsg) },
                            onError = { LogUtils.e(TAG, "validate name error") }
                    )
        }

        launchDisposable {
            RxView.input(edtEmail)
                    .map {
                        return@map ValidateUtils.validateEmail(it, applicationContext)
                    }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onNext = { errorMsg -> emailError.set(errorMsg) },
                            onError = { LogUtils.e(TAG, "validate email error") }
                    )
        }

        launchDisposable {
            RxView.input(edtPassword)
                    .map {
                        return@map ValidateUtils.validatePassword(it, applicationContext)
                    }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onNext = { errorMsg ->
                                passwordError.set(errorMsg)
                                validatePassword()
                            },
                            onError = { LogUtils.e(TAG, "validate password error") }
                    )
        }

        launchDisposable {
            RxView.input(edtConfirmPassword)
                    .map {
                        return@map ValidateUtils.validateConfirmPassword(it, applicationContext)
                    }
                    .withScheduler(baseScheduleProvider)
                    .subscribeBy(
                            onNext = { errorMsg ->
                                confirmPasswordError.set(errorMsg)
                                validatePassword()
                            },
                            onError = { LogUtils.e(TAG, "validate password error") }
                    )
        }
    }

    private fun validatePassword() {
        val errConfirmPassword = getString(R.string.err_msg_confirm_password_incorrect)
        if (confirmPasswordError.get()?.isNotBlank() == true && confirmPasswordError.get() != errConfirmPassword) {
            return
        }
        val password = edtPassword.text.toString()
        val confirmPassword = edtConfirmPassword.text.toString()
        if (password.isBlank() || confirmPassword.isBlank()) {
            return
        }
        if (password != confirmPassword) {
            confirmPasswordError.set(errConfirmPassword)
        } else {
            confirmPasswordError.set("")
        }
    }


    companion object {

        private const val TAG = "RegisterActivity"

        fun getIntent(context: Context): Intent = Intent(context, RegisterActivity::class.java)

    }
}
