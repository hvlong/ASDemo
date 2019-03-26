package com.challenge.aspire.utils.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.challenge.aspire.R
import com.challenge.aspire.utils.AnimateType

fun Fragment.replaceFragment(@IdRes containerId: Int, fragment: Fragment,
        addToBackStack: Boolean = true, tag: String = fragment::class.java.simpleName,
        animateType: AnimateType = AnimateType.FADE) {
    fragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType = animateType)
}

fun Fragment.addFragment(@IdRes containerId: Int, fragment: Fragment,
        addToBackStack: Boolean = true,
        tag: String = fragment::class.java.simpleName,
        animateType: AnimateType = AnimateType.FADE) {
    fragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType = animateType)
}

fun Fragment.addChildFragment(@IdRes containerId: Int, fragment: Fragment,
        fragmentManager: FragmentManager,
        addToBackStack: Boolean = true,
        tag: String = fragment::class.java.simpleName,
        animateType: AnimateType = AnimateType.FADE) {
    fragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType = animateType)
}

fun Fragment.isCanPopBackStack(): Boolean {
    fragmentManager.notNull {
        val isShowPreviousPage = it.backStackEntryCount > 0
        if (isShowPreviousPage) {
            it.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
    return false
}

fun isExistFragment(fragmentManager: FragmentManager, tag: String): Boolean {
    val fragment = fragmentManager.findFragmentByTag(tag)
    return fragment != null
}


/**
 * Runs a FragmentTransaction, then calls commitAllowingStateLoss().
 */
inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit,
        animateType: AnimateType = AnimateType.FADE) {
    beginTransaction().apply {
        setCustomAnimations(this, animateType)
        action()
    }.commitAllowingStateLoss()
}

fun setCustomAnimations(transaction: FragmentTransaction,
        animateType: AnimateType = AnimateType.FADE) {
    when (animateType) {
        AnimateType.FADE -> {
            transaction.setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
        }
        AnimateType.SLIDE_TO_RIGHT -> {
        }
        AnimateType.BOTTOM_UP -> {
            transaction.setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
        }
    }
}