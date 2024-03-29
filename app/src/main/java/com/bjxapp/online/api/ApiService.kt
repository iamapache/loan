package com.bjxapp.online.api

import com.bjxapp.online.ui.bean.ApiResponse
import com.bjxapp.online.ui.bean.UserInfo
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    companion object {
        const val SERVER_URL = "http://149.129.217.31:1360/"

    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(SERVER_URL)
    suspend fun post(@FieldMap fields: Map<String, String>
    ): ApiResponse<UserInfo>


    @Multipart
    @POST(SERVER_URL)
    suspend fun post2(@Part("noncestr") name: RequestBody, @Part("userPhone") name2: RequestBody, @Part("reqSign") name3: RequestBody,
    ): ApiResponse<UserInfo>
}