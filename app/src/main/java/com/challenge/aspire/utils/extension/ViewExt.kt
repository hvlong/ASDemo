package com.challenge.aspire.utils.extension

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import com.challenge.aspire.utils.LogUtils

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun Context.showToast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

@SuppressLint("SetJavaScriptEnabled")
fun WebView.loadIframeDynamicMap(embedUrl: String?, progressBar: ProgressBar?) {
    if (embedUrl.isNullOrEmpty()) return
    if (progressBar == null) {
        webViewClient = WebViewClient()
    } else {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }
    }
    with(settings) {
        javaScriptEnabled = true
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }
    val data = "<iframe " +
            "src=\"$embedUrl\"" +
            "width=\"100%!important\"" +
            "height=\"350px\"" +
            "frameborder=\"0\" " +
            "style=\"border:0\"" +
            "allowfullscreen></iframe>"
    loadData(data, "text/html; charset=utf-8", "utf-8")
    LogUtils.d("EmbedUrl", embedUrl)
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.loadWebViewUrl(url: String?, progressBar: ProgressBar?) {
    if (url.isNullOrEmpty()) return
    if (progressBar == null) {
        webViewClient = WebViewClient()
    } else {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }
    }
    with(settings) {
        javaScriptEnabled = true
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }
    loadUrl(url)
    LogUtils.d("loadWebViewUrl", url)
}