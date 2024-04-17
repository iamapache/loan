package com.lemon.now.ui

import com.lemon.now.ui.bean.AWSBean
import com.lemon.now.ui.bean.AuthInfoBean
import com.lemon.now.ui.bean.CommonData
import com.lemon.now.ui.bean.ExtenBean
import com.lemon.now.ui.bean.FBbean
import com.lemon.now.ui.bean.HomeBean
import com.lemon.now.ui.bean.LoginBean
import com.lemon.now.ui.bean.ORCBean
import com.lemon.now.ui.bean.OrderDetailBean
import com.lemon.now.ui.bean.OrderListBean
import com.lemon.now.ui.bean.OrderStatusBean
import com.lemon.now.ui.bean.OrderSuccesBean
import com.lemon.now.ui.bean.PayResultBean
import com.lemon.now.ui.bean.PruductBean
import retrofit2.http.*

interface ApiService {

    companion object {
        const val SERVER_URL = "http://149.129.217.31:1360/"

        const val loginToken = "loginToken"
        const val channel = "channel"
        const val firsttime = "firsttime"
        const val phone = "phone"
    }

    @FormUrlEncoded
    @POST(("DJEaN/sKjcv"))
    suspend fun homelist(@FieldMap fields: Map<String, String>
    ): PruductBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/hpcrsE"))
    suspend fun getuserinfo(@FieldMap fields: Map<String, String>
    ): HomeBean

    @FormUrlEncoded
    @POST(("DJEaN/IuZlC"))
    suspend fun login(@FieldMap fields: Map<String, String>
    ): LoginBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/xsKknX"))
    suspend fun aws(@FieldMap fields: Map<String, String>
    ): AWSBean
    @FormUrlEncoded
    @POST(("DJEaN/uUSrg"))
    suspend fun code(@FieldMap fields: Map<String, String>
    ): CommonData

    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/FrIhrw"))
    suspend fun fblist(@FieldMap fields: Map<String, String>
    ): FBbean

    @FormUrlEncoded
    @POST(("DJEaN/KjklT"))
    suspend fun firsttime(@FieldMap fields: Map<String, String>
    ): CommonData
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/qOMhdX"))
    suspend fun submitAuth(@FieldMap fields: HashMap<String, String>
    ): CommonData


    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/sRbCIs"))
    suspend fun faceStep(@FieldMap fields: HashMap<String, String>
    ): CommonData

    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/oMEODB"))
    suspend fun fbsave(@FieldMap fields: HashMap<String, String>
    ): CommonData

    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/mNsGUr"))
    suspend fun checkorderstatus(@FieldMap fields: HashMap<String, String>
    ): OrderStatusBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/grtIgc"))
    suspend fun orderlist(@FieldMap fields: HashMap<String, Int>
    ): OrderListBean

    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/vdLoMA"))
    suspend fun orderdetail(@FieldMap fields: HashMap<String, String>
    ): OrderDetailBean

    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/hmrOCd"))
    suspend fun ocr(@FieldMap fields: Map<String, String>
    ): ORCBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/ePidte"))
    suspend fun authinfo(@FieldMap fields: Map<String, String>
    ): AuthInfoBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/hzGENf"))
    suspend fun bank(@FieldMap fields: Map<String, String>
    ): CommonData
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/mBUjul"))
    suspend fun extendetail(@FieldMap fields: Map<String, String>
    ): ExtenBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/yvNawc"))
    suspend fun repay(@FieldMap fields: Map<String, String>
    ): PayResultBean
    @FormUrlEncoded
    @POST(("DJEaN/KIwWe/mOuFrg"))
    suspend fun submitOrder(@FieldMap fields: Map<String, String>
    ): OrderSuccesBean
    @FormUrlEncoded
    @POST(("DJEaN/VjqpF"))
    suspend fun logout(@FieldMap fields: Map<String, String>
    ): CommonData
}