package com.bjxapp.online.base.etx.net

import android.os.Build
import com.bjxapp.online.ui.bean.DeviceInfo
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
        builder.addHeader("deviceInfo", "{  \"androidIdOrUdid\": \"AD0A79D4-E302-4A33-8E45-DF754DD80204\",  \"appVersion\": \"1.0.0\",  \"bag\": \"com.india.very.look.name.app\",  \"brand\": \"Xiaomi\",  \"channel\": \"\",  \"deviceModel\": \"Simulator\",  \"gaidOrIdfa\": \"\",  \"operationSys\": \"android\",  \"osVersion\": \"17.0\"}").build()
        return chain.proceed(builder.build())
    }
}