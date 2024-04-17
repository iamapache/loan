package com.lemon.now.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.view.CustomDialog2
import com.lemon.now.online.databinding.ActivityFbdetailBinding
import com.lemon.now.ui.adapter.SimpleAdapter
import com.lemon.now.ui.bean.ATZAuA4z
import com.lemon.now.ui.model.OrderViewModel


/**
 *   Lemon Cash
 *  OrderListActivity.java
 *
 */
class FabackDetailActivity : BaseActivity1<OrderViewModel, ActivityFbdetailBinding>() {
    private val adapter: SimpleAdapter by lazy { SimpleAdapter(this,dataList) }
    private val dataList = mutableListOf<String>()
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        var bean = intent.getParcelableExtra<ATZAuA4z>("bean")
        Glide.with(this).load(bean?.pPQGUTNAxoA).into(mViewBind.productLogo)
        mViewBind.pfdroDCductName.text =  bean?.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
        mViewBind.orderid.text =  bean?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t
        mViewBind.orderno.text =  bean?.OQjcAh53o829sGL0
        mViewBind.content.setText(bean?.kTzkWYHHqvl.toString())
        val layoutManager = GridLayoutManager(this,3)
        mViewBind.recyclerView.layoutManager = layoutManager
        mViewBind.recyclerView.adapter = adapter
        val users = bean?.JFF8vDMjgOJHRNlj11
            ?.replace("[", "")
            ?.replace("]", "")
            ?.replace("\"", "")
            ?.split(",")
        users?.let { dataList.addAll(it) }
        adapter.notifyDataSetChanged()
    }

    override fun createObserver() {

        mViewModel.commondata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val dialog = CustomDialog2(this@FabackDetailActivity)
                dialog.setCancelable(false)
                dialog.setConfirmCallback {
                    finish()
                }
                dialog.show()
            }
        })

    }

}