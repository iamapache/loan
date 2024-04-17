package com.lemon.now.base.etx.net

import SPUtil
import android.content.Intent
import com.google.gson.Gson
import com.lemon.now.base.App
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.activity.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class TokenOutInterceptor : Interceptor {

    val gson: Gson by lazy { Gson() }

    @kotlin.jvm.Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return if (response.body != null && response.body!!.contentType() != null) {
            val mediaType = response.body!!.contentType()
            val string = response.body!!.string()
            val responseBody = ResponseBody.create(mediaType, string)
            val apiResponse = gson.fromJson(string, ApiResponse::class.java)
            if (apiResponse.rZ81DSU7WU4hny4ukGHljvjO41bfB == -1) {
                var loginToken:   String by SPUtil(ApiService.loginToken, "")
                loginToken=""
                var phone:   String by SPUtil(ApiService.phone, "")
                phone=""
                val intent =Intent(App.instance, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("msg",1)
                App.instance.startActivity(intent)
            }
            response.newBuilder().body(responseBody).build()
        } else {
            response
        }
    }
}