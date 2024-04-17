package com.lemon.now.ui.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.databinding.ActivityFblistBinding
import com.lemon.now.ui.adapter.FBAdapter
import com.lemon.now.ui.bean.ATZAuA4z
import com.lemon.now.ui.model.OrderViewModel

/**
 *   Lemon Cash
 *  FabackListActivity.java
 *
 */
class FabackListActivity : BaseActivity1<OrderViewModel, ActivityFblistBinding>(){
    private val adapter: FBAdapter by lazy { FBAdapter(this, dataList) }
    private val dataList = mutableListOf<ATZAuA4z>()
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        loadNextPage()
        val layoutManager = LinearLayoutManager(this)
        mViewBind.recyclerView.layoutManager = layoutManager
        mViewBind.recyclerView.adapter = adapter

    }

    private fun loadNextPage() {
        mViewModel.fblist()
    }

    override fun createObserver() {

        mViewModel.fbdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                dataList.addAll(it.aTZAuA4z)
                adapter.notifyDataSetChanged()
            }
        })
    }
}