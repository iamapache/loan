package com.bjxapp.online.ui.model

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
class HomeViewModel  : BaseViewModel() {
    fun post() {
        val fields = hashMapOf("usNNNeNrPhone" to "123"
        )
        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), "541D327488139B9B49EF85E772CAF692")
        val age = RequestBody.create("text/plain".toMediaTypeOrNull(), "qwqw")
        val reqSign = RequestBody.create("text/plain".toMediaTypeOrNull(), "123")
        request({ apiService.post(fields)},{
            //请求成功 已自动处理了 请求结果是否正常
        },{
            //请求失败 网络异常，或者请求结果码错误都会回调在这里
        })
    }
}