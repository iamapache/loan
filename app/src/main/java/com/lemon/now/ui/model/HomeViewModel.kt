package com.lemon.now.ui.model

import androidx.lifecycle.MutableLiveData
import com.lemon.now.base.etx.net.apiService
import com.lemon.now.base.etx.net.getData
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.ui.bean.AWSBean
import com.lemon.now.ui.bean.CommonData
import com.lemon.now.ui.bean.HomeBean
import com.lemon.now.ui.bean.OrderStatusBean
import com.lemon.now.ui.bean.OrderSuccesBean
import com.lemon.now.ui.bean.PruductBean
import com.lemon.now.util.SettingUtil

/**
 *   Lemon Cash
 *  LoginRegisterViewModel.java
 *
 */
class HomeViewModel  : BaseViewModel() {
    var result = MutableLiveData<PruductBean>()
    var homebean = MutableLiveData<HomeBean>()
    var bankdata = MutableLiveData<CommonData>()
    var commonData = MutableLiveData<AWSBean>()
    var userFaceStepdata = MutableLiveData<CommonData>()

    var orderstatusdata = MutableLiveData<OrderStatusBean>()

    var submitdata = MutableLiveData<OrderSuccesBean>()

    fun homelist() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.homelist(map)},{
            result.value =it
        },isShowDialog = true)
    }

    fun checkorderstatus(id: String,vpn: String,slots: String,simcount: String) {
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString() ,
            "ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"nJNb2VY6" to id
        )
        getData({ apiService.checkorderstatus(map)},{
            orderstatusdata.value =it
        },isShowDialog = true)
    }

    fun submitOrder(data: String,id: String,vpn: String,slots: String,simcount: String) {
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString(),
            "ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"nJNb2VY6" to id,"wT6Cktpob4DDsf20wcGT5Qr" to data
        )
        getData({ apiService.submitOrder(map)},{
            submitdata.value =it
        },isShowDialog = true,loadingMessage="Your application is being submitted, please do not exit or return.")
    }

    fun aws() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.aws(map)},{
            commonData.value =it
        },isShowDialog = true)
    }

    fun firsttime() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.firsttime(map)},{
        })
    }

    fun faceStep (map: HashMap<String, String>) {
        getData({ apiService.faceStep(map)},{
            userFaceStepdata.value =it
        },isShowDialog = true)
    }
    fun getuserinfo(vpn: String,slots: String,simcount: String) {
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString() ,"ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"UmbYQMS8YVrrXhlZD3SiYMCE3maDZ" to "0"
        )
        getData({ apiService.getuserinfo(map)},{
            homebean.value =it
        },isShowDialog = true)
    }

    fun bank(account: String,name: String,code: String) {
        val map = hashMapOf(
            "DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh" to account,"QI7zJuHhm40svgmoacMA41EyG7Onn" to name
            ,"NfdUWBoWLpCLqyf83El" to code
        )
        getData({ apiService.bank(map)},{
            bankdata.value =it
        },isShowDialog = true)
    }
}