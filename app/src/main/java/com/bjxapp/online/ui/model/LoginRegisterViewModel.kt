package com.bjxapp.online.ui.model

import com.bjxapp.online.base.etx.databind.StringObservableField
import com.bjxapp.online.base.etx.net.apiService
import com.bjxapp.online.base.etx.net.request
import com.bjxapp.online.base.viewmodel.BaseViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


/**
 *   Lemon Cash
 *  LoginRegisterViewModel.java
 *
 */
class LoginRegisterViewModel  : BaseViewModel() {
    var username = StringObservableField()
    var password = StringObservableField()

    fun post(userPhone: String, password: String) {
        val fields = hashMapOf(
            "noncestr" to "abcdefg",
            "userPhone" to "111111","reqSign" to "5E75BDFC40A19A9BBEFADFCA4890C804"
        )
        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), "abcdefg")
        val age = RequestBody.create("text/plain".toMediaTypeOrNull(), "111111")
        val reqSign = RequestBody.create("text/plain".toMediaTypeOrNull(), "5E75BDFC40A19A9BBEFADFCA4890C804")
        request({apiService.post2(name,age,reqSign)},{
            //请求成功 已自动处理了 请求结果是否正常
        },{
            //请求失败 网络异常，或者请求结果码错误都会回调在这里
        })
    }
}