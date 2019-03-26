package com.challenge.aspire.screen.verifyAuth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.challenge.aspire.App
import com.challenge.aspire.R
import com.challenge.aspire.base.BaseActivity
import com.challenge.aspire.base.adapter.OnItemClickListener
import com.challenge.aspire.data.model.Image
import com.challenge.aspire.databinding.ActivityVerifyAuthBinding
import com.challenge.aspire.screen.home.HomeActivity
import com.challenge.aspire.utils.FileUtils
import com.challenge.aspire.utils.PermissionUtils
import com.challenge.aspire.utils.PickerUtils
import com.challenge.aspire.utils.RotateTransformationUtils
import com.challenge.aspire.utils.extension.notNull
import com.challenge.aspire.utils.widget.dialog.DialogAlert
import kotlinx.android.synthetic.main.activity_verify_auth.*
import java.io.File
import java.io.IOException
import javax.inject.Inject


class VerifyAuthActivity : BaseActivity(), OnItemClickListener<Image>, VerifyAuthContract.View {

    @Inject
    lateinit var presenter: VerifyAuthPresenter

    var fileNameIdentity = ObservableField<String>("")
    var fileSelfie: File? = null

    private var fileIdentity: File? = null
    private var imageList: MutableList<Image> = mutableListOf()

    private val imageAdapter: ImageAdapter by lazy {
        ImageAdapter(this).apply {
            registerItemClickListener(this@VerifyAuthActivity)
        }
    }

    override fun initDagger() = (applicationContext as App).appComponent.inject(this)

    override fun initView(savedInstanceState: Bundle?) {
        val binding = DataBindingUtil.setContentView<ActivityVerifyAuthBinding>(this,
                R.layout.activity_verify_auth)
        binding.viewModel = this
    }

    override fun initPresenter() {
        presenter.onAttach(this)
    }

    override fun initialize() {
        rvImageList.apply {
            val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = mLayoutManager
            adapter = imageAdapter
        }
    }

    override fun onDestroy() {
        imageAdapter.unRegisterItemClickListener()
        super.onDestroy()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionUtils.hasPermissionAccessCamera(this) && requestCode == REQUEST_CODE_ACCESS_CAMERA) {
            fileIdentity = FileUtils.getOutputMediaFile(this@VerifyAuthActivity)
            fileIdentity.notNull {
                PickerUtils.startCamera(this, it, REQUEST_CODE_TAKE_IDENTITY)
                return
            }
        }
        if (PermissionUtils.hasPermissionAccessCamera(this) && requestCode == REQUEST_CODE_ACCESS_CAMERA_SELFIE) {
            fileSelfie = FileUtils.getOutputMediaFile(this@VerifyAuthActivity)
            fileSelfie.notNull {
                PickerUtils.startCamera(this, it, REQUEST_CODE_SELFIES)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when(requestCode) {
            REQUEST_CODE_TAKE_IDENTITY -> {
                try {
                    fileIdentity.notNull {
                        val bitmap: Bitmap = MediaStore.Images.Media
                                .getBitmap(contentResolver, Uri.fromFile(it))
                        val rotation = FileUtils.getRotationFromCamera(this, Uri.fromFile(it)).toFloat()
                        Glide.with(this).asBitmap()
                                .load(bitmap)
                                .apply(RequestOptions().transform(
                                        RotateTransformationUtils(this, rotation)))
                                .into(imvIdentity)
                        fileNameIdentity.set(it.name)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            REQUEST_CODE_SELFIES -> {
                try {
                    fileSelfie.notNull {
                        val image = Image()
                        image.name = it.name
                        image.uri = Uri.fromFile(it)
                        image.file = fileSelfie
                        imageList.add(0, image)
                        imageAdapter.addItem(image, 0)
                        imageAdapter.notifyDataSetChanged()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun onBack() {
        onBackPressed()
    }

    fun onAttachIdentityClick() {
        if (PermissionUtils.hasPermissionAccessCamera(this)) {
            fileIdentity = FileUtils.getOutputMediaFile(this@VerifyAuthActivity)
            fileIdentity.notNull {
                PickerUtils.startCamera(this, it, REQUEST_CODE_TAKE_IDENTITY)
            }
        } else {
            PermissionUtils.requestPermissionAccessCamera(this, REQUEST_CODE_ACCESS_CAMERA)
        }
    }

    fun onAttachSelfiesClick() {
        if (PermissionUtils.hasPermissionAccessCamera(this)) {
            fileSelfie = FileUtils.getOutputMediaFile(this@VerifyAuthActivity)
            fileSelfie.notNull {
                PickerUtils.startCamera(this, it, REQUEST_CODE_SELFIES)
            }
        } else {
            PermissionUtils.requestPermissionAccessCamera(this, REQUEST_CODE_ACCESS_CAMERA_SELFIE)
        }
    }

    fun onSubmitInfoClick() {
        if (fileIdentity == null) {
            showAlertDialog(message = getString(R.string.err_msg_please_add_identity))
            return
        }
        if (imageList.size == 0) {
            showAlertDialog(message = getString(R.string.err_msg_please_add_image_selfie))
            return
        }
        presenter.verifyAuth(fileIdentity!!, imageList)
    }

    override fun onItemViewClick(item: Image, position: Int) {

    }

    override fun onVerifyAuthSuccess() {
        showAlertDialog(message = getString(R.string.msg_verify_info_success), listener = object : DialogAlert.Companion.OnButtonClickedListener{
            override fun onPositiveClicked() {
                startActivity(HomeActivity.getIntent(this@VerifyAuthActivity))
                finish()
            }
        })
    }

    companion object {

        const val REQUEST_CODE_ACCESS_CAMERA = 52
        const val REQUEST_CODE_ACCESS_CAMERA_SELFIE = 53

        private const val REQUEST_CODE_TAKE_IDENTITY = 102
        private const val REQUEST_CODE_SELFIES = 103

        fun getIntent(context: Context): Intent = Intent(context, VerifyAuthActivity::class.java)

    }
}
