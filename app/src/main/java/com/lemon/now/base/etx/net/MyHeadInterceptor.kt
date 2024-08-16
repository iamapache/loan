package com.lemon.now.base.etx.net

import SPUtil
import android.os.Build
import android.provider.Settings
import com.google.gson.Gson
import com.lemon.now.base.App
import com.lemon.now.online.BuildConfig
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.bean.DeviceInfo
import com.lemon.now.util.SettingUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.UUID

class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("lang", "en").build()
        var loginToken:   String by SPUtil(ApiService.loginToken, "")
        builder.addHeader("token", loginToken).build()
        var channel: String by SPUtil(ApiService.channel, "Other")
        var googleAdid:   String by SPUtil("googleadid", UUID.randomUUID().toString())
        val androidId = Settings.Secure.getString(App.instance.getContentResolver(), Settings.Secure.ANDROID_ID)
        var deviceInfo = DeviceInfo(
            androidId, BuildConfig.VERSION_NAME.toString(), BuildConfig.APPLICATION_ID,Build.BRAND,
            channel,Build.MODEL,googleAdid,
            SettingUtil.getCurrentWifiMacAddress(App.instance)?:"","android",Build.VERSION.RELEASE + "")
        builder.addHeader("deviceInfo", Gson().toJson(deviceInfo)).build()
        return chain.proceed(builder.build())
    }
}