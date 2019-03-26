package com.challenge.aspire.screen.profile

import `in`.gauriinfotech.commons.Commons
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.challenge.aspire.App
import com.challenge.aspire.R
import com.challenge.aspire.base.BaseActivity
import com.challenge.aspire.data.model.User
import com.challenge.aspire.databinding.ActivityProfileBinding
import com.challenge.aspire.screen.verifyAuth.VerifyAuthActivity
import com.challenge.aspire.utils.FileUtils
import com.challenge.aspire.utils.PermissionUtils
import com.challenge.aspire.utils.PickerUtils
import com.challenge.aspire.utils.extension.notNull
import com.challenge.aspire.utils.widget.dialog.DialogAlert
import java.io.File
import javax.inject.Inject


class ProfileActivity : BaseActivity(), ProfileContract.View {

    @Inject
    lateinit var presenter: ProfilePresenter

    var user = ObservableField<User>()
    var fileName = ObservableField<String>("")
    var attchedFile = ObservableField<Boolean>(false)
    var pdfFileAttach: File? = null

    override fun initDagger() = (applicationContext as App).appComponent.inject(this)

    override fun initView(savedInstanceState: Bundle?) {
        val binding = DataBindingUtil.setContentView<ActivityProfileBinding>(this,
                R.layout.activity_profile)
        binding.viewModel = this
    }

    override fun initPresenter() = presenter.onAttach(this)

    override fun initialize() {
        this.presenter.getUserProfile(100)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onGetUserProfileSuccess(response: User?) {
        response.notNull {
            user.set(it)
            user.notifyChange()
        }
    }

    fun onBack() {
        onBackPressed()
    }

    fun onAttachBankStatementClick() {
        if (PermissionUtils.hasPermissionAccessFile(this)) {
            PickerUtils.attachPDFFile(this, REQUEST_CODE_ATTACH_FILE)
        } else {
            PermissionUtils.requestPermissionAccessFile(this, REQUEST_CODE_ACCESS_FILE)
        }
    }

    fun onSubmitBankStatementClick() {
        pdfFileAttach?.let {
            presenter.attachBankStatement(it)
        }?: kotlin.run {
            showAlertDialog(message = getString(R.string.err_msg_please_select_pdf_file))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            grantResults: IntArray) {
        if (PermissionUtils.hasPermissionAccessFile(this) && requestCode == REQUEST_CODE_ACCESS_FILE) {
            PickerUtils.attachPDFFile(this, REQUEST_CODE_ATTACH_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null || data.data == null) {
            return
        }
        when(requestCode) {
            REQUEST_CODE_ATTACH_FILE -> {
                val path: String? = data.data!!.path
                path.notNull {
                    pdfFileAttach  = File(it)
                    fileName.set(pdfFileAttach?.name)
                    attchedFile.set(true)
                }
            }
        }
    }

    override fun onAttachBankStatementSuccess() {
        showAlertDialog(message = getString(R.string.msg_attach_file_success), listener = object : DialogAlert.Companion.OnButtonClickedListener {
            override fun onPositiveClicked() {
                startActivity(VerifyAuthActivity.getIntent(this@ProfileActivity))
            }
        })
    }


    companion object {

        private const val TAG = "ProfileActivity"

        private const val REQUEST_CODE_ATTACH_FILE = 101

        private const val REQUEST_CODE_ACCESS_FILE = 50


        fun getIntent(context: Context): Intent = Intent(context, ProfileActivity::class.java)

    }

}
