package com.lemon.now.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.databinding.ActivityOrdertobeBinding
import com.lemon.now.ui.bean.M7CdaEiz0WPh1Cs3iyzkg6Od
import com.lemon.now.ui.model.OrderViewModel
import com.lemon.now.util.SettingUtil

/**
 *   Lemon Cash
 *  OrderListActivity.java
 *
 */
class OrderTobeActivity : BaseActivity1<OrderViewModel, ActivityOrdertobeBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        var type = intent.getIntExtra("type", 3)
        var title = intent.getStringExtra("title")
        mViewBind.back.setOnClickListener {
            finish()
        }
        if (type == 3) {
            mViewBind.lltype.visibility = View.GONE
        } else {
            mViewBind.lltype.visibility = View.VISIBLE
        }
        mViewBind.title.text =  title



        mViewBind.extension.setOnClickListener {
            val intent = intent
            intent.setClass(this@OrderTobeActivity, OrderExtenActivity::class.java)
            startActivity(intent)
        }

        mViewBind.now.setOnClickListener {
            var bean = intent.getParcelableExtra<M7CdaEiz0WPh1Cs3iyzkg6Od>("bean")
            val map = hashMapOf(
                "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to bean?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t.toString(),
                "cUVxr56uydpK5vG1LAkDeCGOAj4gfkDx" to "all","obhH38I5kIj71jbNKPzEFrKkelMHT" to bean?.obhH38I5kIj71jbNKPzEFrKkelMHT.toString()
            )
            mViewModel.repay(map)
        }
        getData()
    }

    private fun getData() {
        var bean = intent.getParcelableExtra<M7CdaEiz0WPh1Cs3iyzkg6Od>("bean")
        val map = hashMapOf(
            "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),
            "KsZ6YErOwuyY5n" to SettingUtil.isVpnConnected(this@OrderTobeActivity)
                .toString(),
            "F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString(),
            "ty4KsDDtUaDQyufZqE" to SettingUtil.getAvailableSimSlots(this@OrderTobeActivity)
                .toString(),
            "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to SettingUtil.getActivatedSimCount(this@OrderTobeActivity)
                .toString(),
            "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to bean?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t.toString()
        )
        mViewModel.orderdetail(map)
    }

    override fun onRestart() {
        super.onRestart()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            getData()
        }, 1000)
    }
    override fun createObserver() {

        mViewModel.payresultdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (it.w9VJPY1aYx65wYI2JTt4s==0) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.aWptfFdiAKe6gU8KTnX6y6VfmFieTcLTd))
                    startActivity(intent)
                }else if (it.w9VJPY1aYx65wYI2JTt4s==1) {
                    val intent = Intent(this@OrderTobeActivity, WebActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("url", it.aWptfFdiAKe6gU8KTnX6y6VfmFieTcLTd)
                    startActivityForResult(intent,111)
                }
            }
        })

        mViewModel.orderdetaildata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if(it.M7CdaEiz0WPh1Cs3iyzkg6Od?.jqfluwFAc8Dm==0){
                    mViewBind.extension.visibility = View.GONE
                }else{
                    mViewBind.extension.visibility = View.VISIBLE
                }
                Glide.with(this).load(it.M7CdaEiz0WPh1Cs3iyzkg6Od?.pPQGUTNAxoA).into(mViewBind.productLogo)
                mViewBind.pfdroDCductName.text =  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
                mViewBind.orderid.text =  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t

                mViewBind.amount.text = "₹ " +  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.WA4R2qnu5lhz7.toString()
                mViewBind.date.text =  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.sktzZnR1tYbzNliF0ZUNLSQLWwz6g3hlyscpj

                mViewBind.receivedamount.text = "₹ " +  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.noNz52nAR9teAGnKBK.toString()
                mViewBind.receivedate.text =  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.H6R0CCiFv5HCTm5AJ63k

                mViewBind.repaymentamount.text = "₹ " +  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.Ei5rFw3ggCfFFxvogcCvdtX.toString()
                mViewBind.repaymentdate.text =  it.M7CdaEiz0WPh1Cs3iyzkg6Od?.obhH38I5kIj71jbNKPzEFrKkelMHT

                mViewBind.charge.text = "₹ " + it.M7CdaEiz0WPh1Cs3iyzkg6Od?.nmcgG0t0bc5aLmVm8HEsDtTVOl4bJZN.toString()
                mViewBind.overdate.text = it.M7CdaEiz0WPh1Cs3iyzkg6Od?.nMPtLLw7ysMUUZ3W6clfFgpCS9C

            }
        })
    }

}