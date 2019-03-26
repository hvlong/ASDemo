package com.challenge.aspire.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

object PickerUtils {

    /**
     * Open folder and choice fileIdentity
     */
    fun attachPDFFile(context: Context, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        (context as Activity).startActivityForResult(Intent.createChooser(intent,
                "Select File"), requestCode)
    }

    fun startCamera(context: Context, filePhoto: File, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val photoURI = FileProvider.getUriForFile(context,
                    context.applicationContext.packageName + ".provider", filePhoto)
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                // Create the File where the photo should go
                // Continue only if the File was successfully created
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraIntent.putExtra("return-data", true)
                (context as Activity).startActivityForResult(cameraIntent, requestCode)
            }
        } else {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                // Create the File where the photo should go
                // Continue only if the File was successfully created
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePhoto))
                cameraIntent.putExtra("return-data", true)
                (context as Activity).startActivityForResult(cameraIntent, requestCode)
            }
        }
    }

}