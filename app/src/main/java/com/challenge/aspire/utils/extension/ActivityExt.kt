package com.challenge.aspire.utils.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.challenge.aspire.utils.AnimateType

/**
 * Various extension functions for AppCompatActivity.
 */


fun AppCompatActivity.replaceFragmentInActivity(@IdRes containerId: Int, fragment: Fragment,
        addToBackStack: Boolean = true, tag: String = fragment::class.java.simpleName,
        animateType: AnimateType = AnimateType.FADE) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType = animateType)
}

fun AppCompatActivity.addFragmentToActivity(@IdRes containerId: Int, fragment: Fragment,
        addToBackStack: Boolean = true, tag: String = fragment::class.java.simpleName,
        animateType: AnimateType = AnimateType.FADE) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType = animateType)
}

fun AppCompatActivity.addFragment(@IdRes containerResId: Int, fragment: Fragment,
        fragmentManager: FragmentManager,
        addToBackStack: Boolean = true,
        tag: String = fragment::class.java.simpleName,
        animateType: AnimateType = AnimateType.FADE) {
    fragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerResId, fragment, tag)
    }, animateType = animateType)
}


fun AppCompatActivity.isCanPopBackStack(): Boolean {
    val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 1
    if (isShowPreviousPage) {
        supportFragmentManager.popBackStackImmediate()
    }
    return isShowPreviousPage
}

fun AppCompatActivity.startActivity(@NonNull intent: Intent,
        flags: Int? = null) {
    flags.notNull {
        intent.flags = it
    }
    startActivity(intent)
}

fun AppCompatActivity.startActivityAtRoot(context: Context,
        @NonNull clazz: Class<out Activity>, args: Bundle? = null) {
    val intent = Intent(context, clazz)
    args.notNull {
        intent.putExtras(it)
    }
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}


fun AppCompatActivity.startActForResult(@NonNull intent: Intent,
        requestCode: Int, flags: Int? = null) {
    flags.notNull {
        intent.flags = it
    }
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.isExistFragment(fragment: Fragment): Boolean {
    return supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName) != null
}

fun AppCompatActivity.showDialogFragment(fragment: DialogFragment,
        tag: String = fragment::class.java.simpleName) {
    val transaction = supportFragmentManager.beginTransaction()
    fragment.show(transaction, tag)
}

fun dismissDialogFragment(fragmentManager: FragmentManager, tag: String) {
    val fragment = fragmentManager.findFragmentByTag(tag)
    val df = fragment as DialogFragment
    df.dismiss()
}

fun <T : Parcelable> AppCompatActivity.getParcelable(
        key: String): T? = intent?.extras?.getParcelable(key)
