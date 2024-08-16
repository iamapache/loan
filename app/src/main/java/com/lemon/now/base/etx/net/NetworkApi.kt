package com.lemon.now.base.etx.net

import com.google.gson.GsonBuilder
import com.lemon.now.ui.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.SERVER_URL)
}

class NetworkApi : BaseNetworkApi() {

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            addInterceptor(MyHeadInterceptor())
            addInterceptor(TokenOutInterceptor()).addInterceptor( BodyInterceptor())
            getHttpLoggingInterceptor()?.let { addInterceptor(it) }
            connectTimeout(50, TimeUnit.SECONDS)
            readTimeout(50, TimeUnit.SECONDS)
            writeTimeout(50, TimeUnit.SECONDS)
            createSSLSocketFactory()?.let { sslSocketFactory(it,MyTrustManager()) }
            hostnameVerifier(TrustAllHostnameVerifier())
        }
        return builder
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val mMyTrustManager = MyTrustManager()
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(mMyTrustManager), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ssfFactory
    }

    private class MyTrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }


    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }


}



