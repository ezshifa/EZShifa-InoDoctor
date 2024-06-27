package com.app.ehealthai.doctor.ui

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.app.ehealthaidoctor.R


class WebViewActivity : AppCompatActivity() {
    var webView: WebView? = null
    lateinit var progressBarLoading: ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.web)
        progressBarLoading = findViewById(R.id.pbLoadingFileData)
        val intent = intent
        val url = intent.getStringExtra("Url")
        if (url != null) {
            progressBarLoading.visibility =View.VISIBLE
//            val mUrl = "http://docs.google.com/gview?embedded=true&url=$url"
            val mUrl = "drive.google.com/viewerng/viewer?embedded=true&url=$url"
            webView?.loadUrl(mUrl)

            // this will enable the javascript.
            webView?.settings?.javaScriptEnabled = true
            webView?.clearCache(true)
            // Enable plugins (for PDF support)
            webView?.settings?.pluginState = WebSettings.PluginState.ON
            webView?.settings?.builtInZoomControls = true
            webView?.settings?.displayZoomControls = true
            // WebViewClient allows you to handle
            // onPageFinished and override Url loading.

            // WebViewClient allows you to handle
            // onPageFinished and override Url loading.
            webView?.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    // Handle URL loading here, especially if it's a PDF URL
                    return false
                }
                override fun onPageFinished(view: WebView, url: String) {
                    progressBarLoading.visibility = View.GONE
                }
                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    progressBarLoading.visibility = View.GONE
                }
                override fun onReceivedHttpError(
                        view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
                }

                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler,
                                                error: SslError) {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView = null
    }
}