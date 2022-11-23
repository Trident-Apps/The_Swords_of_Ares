package com.gameinsight.gplay.islan.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal
import com.palominolabs.http.url.UrlBuilder
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

class AresViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var sourceParam: String
    lateinit var afIdParam: String
    val dpLiveData: MutableLiveData<String> =
        MutableLiveData<String>()

    val afLiveData: MutableLiveData<MutableMap<String, Any>?> =
        MutableLiveData<MutableMap<String, Any>?>()


    fun loadDp(ctx: Context) {
        AppLinkData.fetchDeferredAppLinkData(ctx) {
            dpLiveData.postValue(it?.targetUri.toString())
//            dpLiveData.postValue("myapp://test1/test2/test3/test4/test5")
            Log.d("QQQ", "it's dp - ${it?.targetUri.toString()}")
        }
    }

    val mockData: MutableMap<String, Any>? = mutableMapOf(
        "af_status" to "Non-organic",
        "media_source" to "testSource",
        "campaign" to "test1_test2_test3_test4_test5",
        "adset" to "testAdset",
        "adset_id" to "testAdsetId",
        "campaign_id" to "testCampaignId",
        "orig_cost" to "1.22",
        "af_site_id" to "testSiteID",
        "adgroup" to "testAdgroup"
    )

    fun loadAF(ctx: Context) {
        AppsFlyerLib.getInstance()
            .init("FGahf3xpSZBJp7uzorcGoV", object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    afLiveData.postValue(p0)
                    Log.d("QQQ", "onConversionDataSuccess")
                }

                override fun onConversionDataFail(p0: String?) {
                    afLiveData.postValue(null)
                    Log.d("QQQ", "onConversionDataFail")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}

                override fun onAttributionFailure(p0: String?) {}
            }, ctx)
        AppsFlyerLib.getInstance().start(ctx)
    }

    fun buildLink(
        dpLink: String,
        afData: MutableMap<String, Any>?,
        gadId: String,
        activity: Context
    ): String {
        when (dpLink) {
            "null" -> {
                sourceParam = afData?.get("media_source").toString()
                afIdParam = AppsFlyerLib.getInstance().getAppsFlyerUID(activity).toString()
            }
            else -> {
                sourceParam = "deeplink"
                afIdParam = "null"
            }
        }
//        val link = UrlBuilder.forHost("https", "theswordsofares.fun/theswordofares.php")
//            .queryParam("YlhbOrTIdu", "FfC4bJW5kv")
//            .queryParam("xs6qkCWa1Q", TimeZone.getDefault().id)
//            .queryParam("vNOhI5KciW", gadId)
//            .queryParam("3WS3N4sAyS", dpLink)
//            .queryParam("OoKTWZU1tA", sourceParam)
//            .queryParam("BDSyw325Jv", afIdParam)
//            .queryParam("G4SYa9NTkJ", afData?.get("adset_id").toString())
//            .queryParam("GgfCaydZ0M", afData?.get("campaign_id").toString())
//            .queryParam("wixhs7IwOG", afData?.get("campaign").toString())
//            .queryParam("cirEbcKPpr", afData?.get("adset").toString())
//            .queryParam("EmCuhaaFM5", afData?.get("adgroup").toString())
//            .queryParam("K8sfQGiKql", afData?.get("af_siteid").toString())
//            .queryParam("TL4N9GdBEb", afData?.get("adset_id").toString())
//            .toUrlString()
        val link = "https://theswordsofares.fun/theswordofares.php".toUri().buildUpon().apply {
            appendQueryParameter("YlhbOrTIdu", "FfC4bJW5kv")
            appendQueryParameter("xs6qkCWa1Q", TimeZone.getDefault().id)
            appendQueryParameter("vNOhI5KciW", gadId)
            appendQueryParameter("3WS3N4sAyS", dpLink)
            appendQueryParameter("OoKTWZU1tA", sourceParam)
            appendQueryParameter("BDSyw325Jv", afIdParam)
            appendQueryParameter("G4SYa9NTkJ", afData?.get("adset_id").toString())
            appendQueryParameter("GgfCaydZ0M", afData?.get("campaign_id").toString())
            appendQueryParameter("wixhs7IwOG", afData?.get("campaign").toString())
            appendQueryParameter("cirEbcKPpr", afData?.get("adset").toString())
            appendQueryParameter("EmCuhaaFM5", afData?.get("adgroup").toString())
            appendQueryParameter("K8sfQGiKql", afData?.get("af_siteid").toString())
            appendQueryParameter("TL4N9GdBEb", afData?.get("adset_id").toString())
        }
        return link.toString()
    }

    fun makeTag(dLink: String, afData: MutableMap<String, Any>?) {
        val mCampaign = afData?.get("campaign").toString()
        if (mCampaign == "null" && dLink == "null") {
            OneSignal.sendTag("key2", "organic")
        } else if (dLink != "null") {
            OneSignal.sendTag("key2", dLink.replace("myapp://", "").substringBefore("/"))
        } else if (mCampaign != "null") {
            OneSignal.sendTag("key2", mCampaign.substringBefore("_"))
        }
    }

    fun isDirExist(fileName: String, ctx: Context): Boolean {
        return File(ctx.filesDir, fileName).exists()
    }

    fun saveInfo(ctx: Context, fileName: String, content: String) {
        ctx.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun getInfo(activity: Context, fileName: String): String {
        val fileInputStream: FileInputStream? = activity.openFileInput(fileName)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder = StringBuilder()
        var text: String?
        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            stringBuilder.append(text)
        }
        return stringBuilder.toString()
    }
}































































































