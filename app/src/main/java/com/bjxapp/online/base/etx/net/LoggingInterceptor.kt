package com.bjxapp.online.base.etx.net

import com.bjxapp.online.base.etx.util.logv
import com.bjxapp.online.util.MD5Util
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.UUID


/**
 *   Lemon Cash
 *  LoggingInterceptor.java
 *
 */
class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalRequestBody = originalRequest.body
        if (originalRequestBody is FormBody) {
            val newRequest = modifyFormData(originalRequest)
            return chain.proceed(newRequest)
        }
        return chain.proceed(originalRequest)
    }

    private fun modifyFormData(originalRequest: Request): Request {
        val originalFormBody = originalRequest.body as FormBody
        val parameterNames = mutableListOf<String>()
        val parameterValues = mutableMapOf<String, String>()
        for (i in 0 until originalFormBody.size) {
            val name = originalFormBody.name(i)
            val value = originalFormBody.value(i)
            parameterNames.add(name)
            parameterValues[name] = value
        }
        parameterNames.add("noBBBnBcestr")
        parameterValues["noBBBnBcestr"] = UUID.randomUUID().toString()
        // 按照参数名的 ASCII 码从小到大排序
        parameterNames.sort()
        val concatenatedParams = StringBuilder()
        for (name in parameterNames) {
            val value = parameterValues[name]
            if (value != null) {
                concatenatedParams.append("&").append(name).append("=").append(value)

            }
        }
        concatenatedParams.append("&").append("cashwallet").append("=").append("MSXZ8FNV0VMXA1CNV")
        // 删除开头的 "&"
        if (concatenatedParams.isNotEmpty()) {
            concatenatedParams.deleteCharAt(0)
        }
        concatenatedParams.toString().logv()
        val md5Hash = MD5Util.getMD5(concatenatedParams.toString()).toUpperCase()
        val newRequestBodyBuilder = FormBody.Builder()
        for (name in parameterNames) {
            val value = parameterValues[name]
            if (value != null) {
                newRequestBodyBuilder.add(name, value)
            }
        }
        newRequestBodyBuilder.add("reMMMqMSign", md5Hash)
        val newFormBody = newRequestBodyBuilder.build()
        return originalRequest.newBuilder().post(newFormBody).build()
    }
}