package com.bjxapp.online.api

import com.bjxapp.online.ui.bean.ApiResponse
import com.bjxapp.online.ui.bean.UserInfo
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    companion object {
        const val SERVER_URL = "http://149.129.217.31:1360/TTaEPu/rgvIw/"

    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(SERVER_URL)
    suspend fun post(@FieldMap fields: Map<String, String>
    ): ApiResponse<UserInfo>


    @FormUrlEncoded
    @POST(SERVER_URL)
    suspend fun post2(@Part("reMMMqMSign") name: RequestBody, @Part("noBBBnBcestr") name2: RequestBody, @Part("usNNNeNrPhone") name3: RequestBody,
    ): ApiResponse<UserInfo>
}