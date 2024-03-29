package com.bjxapp.online.base.etx.net

import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import java.security.MessageDigest
import java.util.SortedMap
import java.util.TreeMap


/**
 *   Lemon Cash
 *  RequestBodyInterceptor.java
 *
 */
class RequestBodyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the original request
        val originalRequest = chain.request()

        // Proceed if the request method is POST
        if (originalRequest.method == "POST") {
            // Get the form-data submission parameters
            val params = mapOf(
                "param1" to "value1",
                "param2" to "value2"
                // Add more parameters as needed
            )

            // Sort parameters based on ASCII code
            val sortedParams = TreeMap(params)

            // Encrypt parameters with MD5
            val encryptedParams = encryptParams(sortedParams)

            // Construct the request body manually
            val requestBody = constructRequestBody(encryptedParams)

            // Build a new request with the modified request body
            val newRequest = originalRequest.newBuilder()
                .post(requestBody)
                .build()

            // Proceed with the new request
            return chain.proceed(newRequest)
        }

        // Proceed with the original request if it's not a POST request
        return chain.proceed(originalRequest)
    }

    private fun encryptParams(params: SortedMap<String, String>): Map<String, String> {
        val encryptedParams = mutableMapOf<String, String>()
        params.forEach { (key, value) ->
            encryptedParams[key] = encryptString(value)
        }
        return encryptedParams
    }

    private fun constructRequestBody(params: Map<String, String>): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        params.forEach { (key, value) ->
            builder.addFormDataPart(key, value)
        }
        return builder.build()
    }

    private fun encryptString(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}