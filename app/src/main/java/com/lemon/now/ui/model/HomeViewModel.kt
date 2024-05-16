package com.lemon.now.ui.model

import ToastUtils
import androidx.lifecycle.MutableLiveData
import com.lemon.now.base.App
import com.lemon.now.base.etx.net.apiService
import com.lemon.now.base.etx.net.getData
import com.lemon.now.base.etx.net.getData2
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
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
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
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }

    fun submitOrder(data: String,id: String,vpn: String,slots: String,simcount: String) {
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString(),
            "ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"nJNb2VY6" to id,"wT6Cktpob4DDsf20wcGT5Qr" to data
        )
        getData2({ apiService.submitOrder(map)},{
            submitdata.value =it
        },{
            val errorMsg =
                OrderSuccesBean(0, ArrayList(),
                    3,
                    it.errorMsg,
                )
            submitdata.value =errorMsg
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = false,loadingMessage="Your application is being submitted, please do not exit or return.")
    }

    fun aws() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.aws(map)},{
            commonData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
    fun aws2() {
        val map = hashMapOf("" to ""
        )
        getData2({ apiService.aws(map)},{
            commonData.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = false)
    }
    fun firsttime() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.firsttime(map)},{
        })
    }

    fun faceStep (map: HashMap<String, String>) {
        getData2({ apiService.faceStep(map)},{
            userFaceStepdata.value =it
        },{
            val errorMsg =
                CommonData(
                    3,
                   it.errorMsg,
                )
            userFaceStepdata.value =errorMsg
        },isShowDialog = false)
    }
    fun getuserinfo(isShowDialog: Boolean,vpn: String,slots: String,simcount: String) {
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),"KsZ6YErOwuyY5n" to vpn
            ,"F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString() ,"ty4KsDDtUaDQyufZqE" to slots ,
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to simcount,"UmbYQMS8YVrrXhlZD3SiYMCE3maDZ" to "0"
        )
        if (isShowDialog){
            getData({ apiService.getuserinfo(map)},{
                homebean.value =it
            },{
                ToastUtils.showShort(App.instance,it.errorMsg)
            },isShowDialog = isShowDialog)
        }else{
            getData2({ apiService.getuserinfo(map)},{
                homebean.value =it
            },{
                ToastUtils.showShort(App.instance,it.errorMsg)
            },isShowDialog = isShowDialog)
        }

    }


    fun bank(account: String,name: String,code: String) {
        val map = hashMapOf(
            "DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh" to account,"QI7zJuHhm40svgmoacMA41EyG7Onn" to name
            ,"NfdUWBoWLpCLqyf83El" to code
        )
        getData({ apiService.bank(map)},{
            bankdata.value =it
        },{
            ToastUtils.showShort(App.instance,it.errorMsg)
        },isShowDialog = true)
    }
}