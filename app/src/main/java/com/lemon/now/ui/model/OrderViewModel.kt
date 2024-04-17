package com.lemon.now.ui.model

import androidx.lifecycle.MutableLiveData
import com.lemon.now.base.etx.net.apiService
import com.lemon.now.base.etx.net.getData
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.ui.bean.AWSBean
import com.lemon.now.ui.bean.CommonData
import com.lemon.now.ui.bean.ExtenBean
import com.lemon.now.ui.bean.FBbean
import com.lemon.now.ui.bean.OrderDetailBean
import com.lemon.now.ui.bean.OrderListBean
import com.lemon.now.ui.bean.OrderStatusBean
import com.lemon.now.ui.bean.PayResultBean
import com.lemon.now.util.SettingUtil

/**
 *   Lemon Cash
 *  OrderViewModel.java
 *
 */
class OrderViewModel : BaseViewModel() {
    var orderdata = MutableLiveData<OrderListBean>()
    var commondata = MutableLiveData<CommonData>()
    var orderstatusdata = MutableLiveData<OrderStatusBean>()
    var fbdata = MutableLiveData<FBbean>()
    var payresultdata = MutableLiveData<PayResultBean>()
    var orderdetaildata = MutableLiveData<OrderDetailBean>()

    var extendata = MutableLiveData<ExtenBean>()
    fun orderlist (map: HashMap<String, Int>,isShowDialog:Boolean) {
        getData({ apiService.orderlist(map)},{
            orderdata.value =it
        },isShowDialog = isShowDialog)
    }


    fun orderdetail (map: HashMap<String, String>) {
        getData({ apiService.orderdetail(map)},{
            orderdetaildata.value =it
        },isShowDialog = true)
    }

    fun extendetail (map: HashMap<String, String>) {
        getData({ apiService.extendetail(map)},{
            extendata.value =it
        },isShowDialog = true)
    }

    fun repay (map: HashMap<String, String>) {
        getData({ apiService.repay(map)},{
            payresultdata.value =it
        },isShowDialog = true)
    }


    var awsData = MutableLiveData<AWSBean>()
    fun aws() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.aws(map)},{
            awsData.value =it
        },isShowDialog = true)
    }


    fun fblist() {
        val map = hashMapOf("" to ""
        )
        getData({ apiService.fblist(map)},{
            fbdata.value =it
        },isShowDialog = true)
    }

    fun fbsave (map: HashMap<String, String>) {
        getData({ apiService.fbsave(map)},{
            commondata.value =it
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
}