package com.challenge.aspire.utils

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Pair
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.challenge.aspire.R
import com.challenge.aspire.utils.extension.notNull
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object PermissionUtils {

    private val permissionCamera = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var permissionArrayFile = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /**
     * Check permission access camera
     *
     * @return true if allow access camera, otherwise return false
     */
    fun hasPermissionAccessCamera(context: Context): Boolean {
        for (permission in permissionCamera) {
            if (ContextCompat.checkSelfPermission(context,
                            permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * @param context context
     * @return true if allow permission read and write storage fileIdentity, otherwise false
     */
    fun hasPermissionAccessFile(context: Context): Boolean {
        for (permission in permissionArrayFile) {
            if (ContextCompat.checkSelfPermission(context,
                            permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun requestPermissionAccessFile(context: Context, requestCode: Int) {
        ActivityCompat.requestPermissions(context as Activity, permissionArrayFile, requestCode)
    }

    fun requestPermissionAccessCamera(context: Context, requestCode: Int) {
        ActivityCompat.requestPermissions(context as Activity, permissionCamera, requestCode)
    }

}