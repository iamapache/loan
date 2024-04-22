package com.lemon.now.ui.activity

import ToastUtils
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.databinding.ActivityOrdersuccesBinding
import com.lemon.now.ui.bean.O2Hvk1wvAGN
import com.lemon.now.ui.model.OrderViewModel
import com.lemon.now.util.SettingUtil
import kotlin.random.Random


class OrderSuccesActivity : BaseActivity1<OrderViewModel, ActivityOrdersuccesBinding>() {
    var productdd = ""
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.finish.setOnClickListener {
            finish()
        }
        val receivedList = intent.getParcelableArrayListExtra<O2Hvk1wvAGN>("list")
        var current = 0
        if(receivedList?.size!! >0){
            receivedList?.get(0)?.let {
                updata(it)
                productdd = it.nJNb2VY6
                mViewBind.layoutProduct.fendata.text = (current + 1).toString() + "/" + receivedList.size
            }
            receivedList?.let {
                mViewBind.layoutProduct.profileBack.setOnClickListener {
                    if (current > 0 && current <= receivedList.size - 1) {
                        current--
                        mViewBind.layoutProduct.fendata.text =
                            (current + 1).toString() + "/" + receivedList.size
                        updata(receivedList.get(current))
                    }

                }
                mViewBind.layoutProduct.profileNext.setOnClickListener {
                    if (current >= 0 && current < receivedList.size - 1) {
                        current++
                        mViewBind.layoutProduct.fendata.text =
                            (current + 1).toString() + "/" + receivedList.size
                        updata(receivedList.get(current))
                    }
                }
            }
            mViewBind.layoutProduct.next.setOnClickListener {
                    mViewModel.checkorderstatus(productdd,
                        SettingUtil.isVpnConnected(this@OrderSuccesActivity).toString(),
                        SettingUtil.getAvailableSimSlots(this@OrderSuccesActivity).toString(),
                        SettingUtil.getActivatedSimCount(this@OrderSuccesActivity).toString())
                }
        }


    }



    override fun createObserver() {
        mViewModel.orderstatusdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                SettingUtil.startOtherActivity(this, it, productdd)
            }else {
                ToastUtils.showShort(this@OrderSuccesActivity, it.vWCgp64OkxPVoGqics)
            }
        })
    }

    private fun updata(it: O2Hvk1wvAGN) {
        mViewBind.layoutProduct.pfdroDCductName.text = it.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
        mViewBind.layoutProduct.productAmount.text =
            "₹ " + it.LUjQkEvfKVJUBeYktoAk8xI0tzPXMKgZWRg
        mViewBind.layoutProduct.productRate.text = it.D5Dm3jAe2ZJNb5bG + "%"
        mViewBind.layoutProduct.productDays.text = it.bkh00lJczrA
        val numbers = listOf(4.8, 4.9, 5.0)
        val randomIndex = Random.nextInt(numbers.size)
        val randomNumber = numbers[randomIndex]
        mViewBind.layoutProduct.score.text = randomNumber.toString()
        productdd = it.nJNb2VY6
        Glide.with(this).load(it.pPQGUTNAxoA).into(mViewBind.layoutProduct.productLogo)
    }
}
