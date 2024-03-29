package com.bjxapp.online.base.etx.net

import android.os.Build
import com.bjxapp.online.ui.bean.DeviceInfo
import com.google.gson.Gson
import com.lemon.now.online.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.UUID

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("lang", "en").build()
        builder.addHeader("token", "").build()
        var deviceInfo = DeviceInfo(UUID.randomUUID().toString(), BuildConfig.VERSION_CODE.toString(), BuildConfig.APPLICATION_ID,Build.BRAND,
            "other",Build.MODEL,"","","Android",Build.VERSION.RELEASE + "")
        builder.addHeader("deviceInfo", Gson().toJson(deviceInfo)).build()
        return chain.proceed(builder.build())
    }
}