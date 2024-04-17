package com.lemon.now.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityOrderpendBinding
import com.lemon.now.ui.bean.M7CdaEiz0WPh1Cs3iyzkg6Od
import com.lemon.now.ui.bean.O2Hvk1wvAGN
import com.lemon.now.ui.model.OrderViewModel
import com.lemon.now.util.SettingUtil
import kotlin.random.Random

/**
 *   Lemon Cash
 *  OrderListActivity.java
 *
 */
class OrderPendingActivity : BaseActivity1<OrderViewModel, ActivityOrderpendBinding>() {
    var productdd = ""

    override fun initView(savedInstanceState: Bundle?) {
        var titlecontent = intent.getStringExtra("titlecontent")
        var title = intent.getStringExtra("title")
        var type = intent.getIntExtra("type",1)
        mViewBind.back.setOnClickListener {
            finish()
        }
        mViewBind.titlecontent.text =  titlecontent
        mViewBind.title.text =  title
        if (type == 1) {
            mViewBind.llbg.setBackgroundResource(R.mipmap.orderdetailbg)
        } else  if (type == 2){
            mViewBind.llbg.setBackgroundResource(R.mipmap.orderdisbu)
        }else  if (type == 5){
            mViewBind.llbg.setBackgroundResource(R.mipmap.orderdisfail)
            mViewBind.llpruduct.visibility= View.GONE
        }else  if (type == 55){
            mViewBind.llbg.setBackgroundResource(R.mipmap.orderdeined)
        }else  if (type == 555){
            mViewBind.llbg.setBackgroundResource(R.mipmap.orderdongj)
            mViewBind.llcontent.visibility= View.GONE
        }



        var bean = intent.getParcelableExtra<M7CdaEiz0WPh1Cs3iyzkg6Od>("bean")
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),
            "KsZ6YErOwuyY5n" to SettingUtil.isVpnConnected(this@OrderPendingActivity)
                .toString(),
            "F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString(),
            "ty4KsDDtUaDQyufZqE" to SettingUtil.getAvailableSimSlots(this@OrderPendingActivity)
                .toString(),
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to SettingUtil.getActivatedSimCount(this@OrderPendingActivity)
                .toString(),
            "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to bean?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t.toString()
        )
        mViewModel.orderdetail(map)
    }
    override fun createObserver() {


        mViewModel.orderdetaildata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                var bean =it.M7CdaEiz0WPh1Cs3iyzkg6Od
                var list =it.o2Hvk1wvAGN
                Glide.with(this).load(bean?.pPQGUTNAxoA).into(mViewBind.productLogo)
                mViewBind.pfdroDCductName.text =  bean?.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV


                mViewBind.amount.text = "₹ " +  bean?.WA4R2qnu5lhz7.toString()
                mViewBind.date.text =  bean?.sktzZnR1tYbzNliF0ZUNLSQLWwz6g3hlyscpj
                mViewBind.totalcount.text =  bean?.DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh

                var current = 0
                list?.let {
                    updata(it[0])
                    productdd = it[0].nJNb2VY6
                    mViewBind.layoutProduct.fendata.text = (current + 1).toString() + "/" + list.size
                }
                list?.let {
                    mViewBind.layoutProduct.profileBack.setOnClickListener {
                        if (current > 0 && current <= list.size - 1) {
                            current--
                            mViewBind.layoutProduct.fendata.text =
                                (current + 1).toString() + "/" + list.size
                            updata(list.get(current))
                        }

                    }
                    mViewBind.layoutProduct.profileNext.setOnClickListener {
                        if (current >= 0 && current < list.size - 1) {
                            current++
                            mViewBind.layoutProduct.fendata.text =
                                (current + 1).toString() + "/" + list.size
                            updata(list.get(current))
                        }
                    }
                }
                mViewBind.layoutProduct.next.setOnClickListener {
                    mViewBind.layoutProduct.next.setOnClickListener {
                        mViewModel.checkorderstatus(productdd,
                            SettingUtil.isVpnConnected(this@OrderPendingActivity).toString(),
                            SettingUtil.getAvailableSimSlots(this@OrderPendingActivity).toString(),
                            SettingUtil.getActivatedSimCount(this@OrderPendingActivity).toString())
                    }
                }

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