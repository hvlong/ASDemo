package com.challenge.aspire.utils.binding

import android.graphics.Bitmap
import android.graphics.Paint
import android.net.Uri
import android.provider.MediaStore
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.challenge.aspire.R
import com.challenge.aspire.utils.FileUtils
import com.challenge.aspire.utils.RotateTransformationUtils
import com.challenge.aspire.utils.extension.notNull
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

object BindingUtils {

    @JvmStatic
    @BindingAdapter("app:currentItem")
    fun setCurrentViewPager(viewPager: ViewPager, currentPage: Int) {
        viewPager.currentItem = currentPage
    }

    @JvmStatic
    @BindingAdapter("app:viewPagerAdapter")
    fun setViewPagerAdapter(viewPager: ViewPager, adapter: PagerAdapter) {
        viewPager.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("app:viewPager")
    fun setViewPagerTabs(tabLayout: TabLayout, viewPager: ViewPager) {
        tabLayout.setupWithViewPager(viewPager, true)
    }

    @JvmStatic
    @BindingAdapter("app:hasFixedSize")
    fun setHasFixedSize(view: RecyclerView, isFixed: Boolean) {
        view.setHasFixedSize(isFixed)
    }

    @JvmStatic
    @BindingAdapter("app:circleImageUrl")
    fun setCircleImageUrl(imageView: ImageView, url: String) {
        url.notNull {
            Glide.with(imageView.context)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("app:imageResourceID")
    fun setImageResourceID(imageView: ImageView, resourceID: Int) {
        if (resourceID == 0) return
        imageView.setImageResource(resourceID)
    }

    @JvmStatic
    @BindingAdapter("app:colorResourceID")
    fun setColorResourceID(view: View, resourceID: Int) {
        if (resourceID == 0) return
        view.setBackgroundResource(resourceID)
    }

    @JvmStatic
    @BindingAdapter("app:visibility")
    fun setVisible(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:invisible")
    fun setInvisible(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("app:isRefreshing")
    fun setSwipeRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean) {
        view.isRefreshing = isRefreshing
    }


    @JvmStatic
    @BindingAdapter("parseHtmlToText")
    fun parseHtmlToText(textView: TextView, bodyData: String) {
        if (TextUtils.isEmpty(bodyData)) {
            return
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(bodyData)
        }
    }

    @JvmStatic
    @BindingAdapter("setTextUnderLine")
    fun setTextUnderLine(textView: TextView, content: String) {
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textView.text = content
    }

    @JvmStatic
    @BindingAdapter("app:adapter")
    fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        view.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("app:errorTextInput")
    fun setErrorTextInput(view: TextInputLayout, error: String) {
        view.error = error
        view.isErrorEnabled = !error.isEmpty()
    }

    @JvmStatic
    @BindingAdapter("app:setImageUri")
    fun setImageUri(imageView: ImageView, uri: Uri?) {
        uri?.let {
            val rotation = FileUtils.getRotationFromCamera(imageView.context, it).toFloat()
            val bitmap: Bitmap = MediaStore.Images.Media
                    .getBitmap(imageView.context.contentResolver, it)
            Glide.with(imageView.context).asBitmap()
                    .load(bitmap)
                    .apply(RequestOptions().transform(
                            RotateTransformationUtils(imageView.context, rotation)))
                    .into(imageView)
        }?: kotlin.run {
            imageView.setImageResource(R.mipmap.ic_launcher)
        }
    }

}