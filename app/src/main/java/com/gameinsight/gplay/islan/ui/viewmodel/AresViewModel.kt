package com.gameinsight.gplay.islan.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

class AresViewModel(app: Application) : AndroidViewModel(app) {
    private lateinit var sourceParam: String
    private lateinit var afIdParam: String
    private lateinit var tagString: String
    private val urlParamsList = mutableListOf<String>()
    val dpLiveData: MutableLiveData<String> =
        MutableLiveData<String>()

    val afLiveData: MutableLiveData<MutableMap<String, Any>?> =
        MutableLiveData<MutableMap<String, Any>?>()


    fun loadDp(ctx: Context) {
        AppLinkData.fetchDeferredAppLinkData(ctx) {
            dpLiveData.postValue(it?.targetUri.toString())
        }
    }

    fun loadAF(ctx: Context) {
        AppsFlyerLib.getInstance()
            .init("FGahf3xpSZBJp7uzorcGoV", object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    afLiveData.postValue(p0)
                }

                override fun onConversionDataFail(p0: String?) {
                    afLiveData.postValue(null)
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}

                override fun onAttributionFailure(p0: String?) {}
            }, ctx)
        AppsFlyerLib.getInstance().start(ctx)
    }

    fun provideParams(dp: String, mutableMap: MutableMap<String, Any>?, ctx: Activity) {
        if (dp == "null") {
            sourceParam = mutableMap?.get("campaign").toString()
            afIdParam = AppsFlyerLib.getInstance().getAppsFlyerUID(ctx).toString()
        } else {
            sourceParam = "deeplink"
            afIdParam = "null"
        }
    }

    fun provideListForUrl(dp: String, af: MutableMap<String, Any>?): List<String> {
        urlParamsList.apply {
            add("YlhbOrTIdu")
            add("FfC4bJW5kv")
            add("xs6qkCWa1Q")
            add(TimeZone.getDefault().id)
            add("vNOhI5KciW")
            add("3WS3N4sAyS")
            add(dp)
            add("OoKTWZU1tA")
            add("BDSyw325Jv")
            add("G4SYa9NTkJ")
            add(af?.get("adset_id").toString())
            add("GgfCaydZ0M")
            add(af?.get("campaign_id").toString())
            add("wixhs7IwOG")
            add(af?.get("campaign").toString())
            add("cirEbcKPpr")
            add(af?.get("adset").toString())
            add("EmCuhaaFM5")
            add(af?.get("adgroup").toString())
            add("K8sfQGiKql")
            add(af?.get("af_siteid").toString())
            add("TL4N9GdBEb")
            add(af?.get("adset_id").toString())
        }
        return urlParamsList
    }

    fun appendStringToUrl(
        gadId: String,
    ): String {
        return BASE_URL.toUri().buildUpon()
            .appendQueryParameter(urlParamsList[0], urlParamsList[1])
            .appendQueryParameter(urlParamsList[2], urlParamsList[3])
            .appendQueryParameter(urlParamsList[4], gadId)
            .appendQueryParameter(urlParamsList[5], urlParamsList[6])
            .appendQueryParameter(urlParamsList[7], sourceParam)
            .appendQueryParameter(urlParamsList[8], afIdParam)
            .appendQueryParameter(urlParamsList[9], urlParamsList[10])
            .appendQueryParameter(urlParamsList[11], urlParamsList[12])
            .appendQueryParameter(urlParamsList[13], urlParamsList[14])
            .appendQueryParameter(urlParamsList[15], urlParamsList[16])
            .appendQueryParameter(urlParamsList[17], urlParamsList[18])
            .appendQueryParameter(urlParamsList[19], urlParamsList[20])
            .appendQueryParameter(urlParamsList[21], urlParamsList[22])
            .build().toString()
    }

    fun makeTag(key: String, value: String) {
        OneSignal.sendTag(key, value)
    }

    fun provideStringForTag(appLinkData: String, appsFlyerLib: String): String {
        if (checkIfNull(appLinkData, appLinkData)) {
            tagString = "organic"
        }
        if (!checkIfOneOf(appLinkData)) {
            tagString = appLinkData.replace("myapp://", "").substringBefore("/")
        }
        if (!checkIfOneOf(appsFlyerLib)) {
            tagString = appLinkData.substringBefore("_")
        }
        return tagString
    }

    private fun checkIfNull(stringF: String, stringA: String): Boolean {
        return stringF == "null" && stringA == "null"
    }

    private fun checkIfOneOf(string: String): Boolean {
        return string == "null"
    }

    fun getValueFromMap(map: MutableMap<String, Any>?): String {
        return map?.get("campaign").toString()
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

    companion object {
        const val BASE_URL = "https://theswordsofares.fun/theswordofares.php"
    }
}































































































