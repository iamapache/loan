package com.bjxapp.online.base.etx.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
data class `2`(
    val androidIdOrUdid: String,
    val appVersion: String,
    val bag: String,
    val brand: String,
    val channel: String,
    val deviceModel: String,
    val gaidOrIdfa: String,
    val operationSys: String,
    val osVersion: String
)
abstract class BaseNetworkApi {

    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder

    /**
     * 配置http
     */
    private val okHttpClient: OkHttpClient
        get() {
            var builder = OkHttpClient.Builder()
            builder = setHttpClientBuilder(builder)
            return builder.build()
        }
}



