package com.lemon.now.ui.model

import ToastUtils
import androidx.lifecycle.MutableLiveData
import com.lemon.now.base.App
import com.lemon.now.base.etx.net.apiService
import com.lemon.now.base.etx.net.getData
import com.lemon.now.base.etx.net.getData2
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.ui.bean.AWSBean
import com.lemon.now.ui.bean.AuthInfoBean
import com.lemon.now.ui.bean.CommonData
import com.lemon.now.ui.bean.HomeBean
import com.lemon.now.ui.bean.ORCBean
import com.lemon.now.util.SettingUtil

/**
 *   Lemon Cash
 *  AuthModel.java
 *
 */
class AuthModel : BaseViewModel() {


    var commonData = MutableLiveData<AWSBean>()
    var authinfoData = MutableLiveData<AuthInfoBean>()
    var orcData = MutableLiveData<ORCBean>()
    var homebean = MutableLiveData<HomeBean>()

    var submitdata = MutableLiveData<CommonData>()
    fun aws() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.aws(map)},{
            commonData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }

    fun submitAuth(map: HashMap<String, String>) {
        getData({ apiService.submitAuth(map)},{
            submitdata.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }

    fun submitAuth2(map: HashMap<String, String>) {
        getData2({ apiService.submitAuth(map)},{
            submitdata.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = false)
    }
    fun getuserinfo(vpn: String,slots: String,simcount: String) {
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString() ,"ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"UmbYQMS8YVrrXhlZD3SiYMCE3maDZ" to "1"
        )
        getData2({ apiService.getuserinfo(map)},{
            homebean.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = false)
    }

    fun authinfo() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.authinfo(map)},{
            authinfoData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
    fun ocr(imageUrl: String, ocrType: String) {
        val map = hashMapOf("jwc8zxW" to imageUrl,"mhGsBfLUrQ5TJnKotbU5ZftOhz" to ocrType
        )
        getData({ apiService.ocr(map)},{
            orcData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
}