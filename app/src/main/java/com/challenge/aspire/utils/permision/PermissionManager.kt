package com.challenge.aspire.utils.permision

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.challenge.aspire.utils.LogUtils
import java.lang.ref.WeakReference

class PermissionManager(ctx: Context? = null) {

    var context: WeakReference<Context?> = WeakReference(ctx)

    fun setupPermissions(context: Context, permission: String, requestCode: Int): Boolean {
        val per = ContextCompat.checkSelfPermission(context, permission)

        if (per != PackageManager.PERMISSION_GRANTED) {
            LogUtils.e(TAG, "Permission to record denied")
            makeRequest(context, permission, requestCode)
        }

        return per == PackageManager.PERMISSION_GRANTED // Has accepted
    }

    fun makeRequest(context: Context, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(context as AppCompatActivity,
                arrayOf(permission),
                requestCode)
    }

    companion object {
        private const val TAG = "PermissionManager"
        private const val PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE
    }
}