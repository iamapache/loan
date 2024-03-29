package com.bjxapp.online.base.etx.net

import com.bjxapp.online.util.MD5Util
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException


/**
 *   Lemon Cash
 *  LoggingInterceptor.java
 *
 */
class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        // 只打印非PATCH请求，因为PATCH请求的RequestBody可能会被重复使用
        if (request.method.equals("PATCH")) {
            return chain.proceed(request)
        }
        val requestBody: RequestBody? = request.body
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            // 确保请求体内容不会被消耗掉
            val bodyString: String = buffer.readUtf8()
            // 打印请求体参数
            println("Request Body: $bodyString${MD5Util.getMD5("noncestr=qwqw&userPhone=123&signKey=iYeXsbQTefsNWaAyFSDRBnkb").toUpperCase()}")

            // 重新构建请求，使用新的请求体
            val newRequest: Request = request.newBuilder()
                .method(request.method, RequestBody.create(requestBody.contentType(), bodyString))
                .build()

            // 继续请求流程
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}