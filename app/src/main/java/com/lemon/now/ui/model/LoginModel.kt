package com.lemon.now.ui.model

import ToastUtils
import androidx.lifecycle.MutableLiveData
import com.lemon.now.base.App
import com.lemon.now.base.etx.databind.StringObservableField
import com.lemon.now.base.etx.net.apiService
import com.lemon.now.base.etx.net.getData
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.ui.bean.CommonData
import com.lemon.now.ui.bean.LoginBean
import com.lemon.now.util.SettingUtil


/**
 *   Lemon Cash
 *  LoginRegisterViewModel.java
 *
 */
class LoginModel  : BaseViewModel() {
    var username = StringObservableField("9130364602")
    var editText = StringObservableField()
    var editText2 = StringObservableField()
    var editText3 = StringObservableField()
    var editText4 = StringObservableField()
    var commonData = MutableLiveData<CommonData>()
    var result = MutableLiveData<LoginBean>()
    fun post(userPhone: String, code: String, vpn: String,slots: String,simcount: String) {
        val map = hashMapOf("RjNq58lUS3qyjEaE1ENjbNF" to userPhone,"kBvZwYsix5p3El0vy04ABrpWR3" to "821350"
            ,"eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString()
        )
        getData({ apiService.login(map)}, {
            result.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
    fun logout() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.logout(map)},{
            commonData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
    fun code(userPhone: String) {
        val map = hashMapOf("RjNq58lUS3qyjEaE1ENjbNF" to userPhone
        )
        getData({ apiService.code(map)},{
            commonData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
}