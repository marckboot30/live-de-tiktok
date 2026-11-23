package com.marck.hordaaovivo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/** Mostra o jogo em tela cheia, com a tela sempre ligada durante a live. */
class MainActivity : Activity() {
    private lateinit var web: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.statusBarColor = Color.parseColor("#0b0f0c")
        window.navigationBarColor = Color.parseColor("#0b0f0c")

        web = WebView(this)
        web.setBackgroundColor(Color.parseColor("#0b0f0c"))
        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        web.webViewClient = object : WebViewClient() {
            // Links (ex.: eulerstream.com) abrem no navegador do celular
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url
                if (url.scheme == "file") return false
                runCatching { startActivity(Intent(Intent.ACTION_VIEW, url)) }
                return true
            }
        }
        setContentView(web)
        if (savedInstanceState != null) web.restoreState(savedInstanceState)
        else web.loadUrl("file:///android_asset/index.html")
        fullScreen()
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) fullScreen()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        web.saveState(outState)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Voltar não fecha o jogo sem querer no meio da live
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        web.destroy()
        super.onDestroy()
    }
}
