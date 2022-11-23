package com.gameinsight.gplay.islan.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import androidx.lifecycle.ViewModelProvider
import com.gameinsight.gplay.islan.databinding.WebAcBinding
import com.gameinsight.gplay.islan.ui.viewmodel.AresViewModel

class WebViewActivity : BaseActivity<WebAcBinding>() {
    lateinit var wmWebView: WebView
    lateinit var loadingUrl: String
    var isLoaded: Boolean = true
    var callback: ValueCallback<Array<Uri?>>? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = initViewBinding()
        setContentView(viewBinding!!.root)
        initViewModel()
        wmWebView = viewBinding!!.web
        val smth = intent.getStringExtra("link")
        Log.d("qqq","web url $smth")
        loadingUrl = smth ?: ""
        with(wmWebView) {
            loadUrl(loadingUrl)
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    isLoaded = false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    CookieManager.getInstance().flush()
                    if (!isLoaded) {
                        naviGateTo(url!!, "")
                    }
                }
            }
            settings.userAgentString = settings.userAgentString.replace("wv", "")
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = false
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri?>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    callback = filePathCallback
                    imageSelector()
                    return true
                }

                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
                    val anotherWebView = WebView(this@WebViewActivity)
                    with(anotherWebView) {
                        settings.javaScriptEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.domStorageEnabled = true
                        settings.setSupportMultipleWindows(true)
                        val transition = resultMsg?.obj as WebView.WebViewTransport
                        transition.webView = this
                        resultMsg.sendToTarget()
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                url: String?
                            ): Boolean {
                                view?.loadUrl(url ?: "")
                                isLoaded = true
                                return true
                            }
                        }
                    }
                    return true
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Activity.RESULT_CANCELED -> {
                callback?.onReceiveValue(null)
                return
            }
            Activity.RESULT_OK -> {
                if (callback == null) return
                callback!!.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        resultCode,
                        data
                    )
                )
                callback = null
            }
        }
    }

    private fun imageSelector() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Image Chooser"), 1
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AresViewModel::class.java]
    }

    override fun initViewBinding(): WebAcBinding = WebAcBinding.inflate(layoutInflater)

    override fun naviGateTo(link: String, adb: String) {
        if (link == "https://theswordsofares.fun/") {
            startActivity(Intent(this, CloakActivity::class.java))
            this@WebViewActivity.finish()
        } else {
            if (!viewModel.isDirExist(FirstActivity.LINK_PATH, this)) {
                viewModel.saveInfo(this, FirstActivity.LINK_PATH, link)
            }
        }
    }
}