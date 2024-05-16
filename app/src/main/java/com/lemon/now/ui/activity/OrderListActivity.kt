package com.lemon.now.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.databinding.ActivityOrderlistBinding
import com.lemon.now.ui.adapter.OrderAdapter
import com.lemon.now.ui.bean.VNozeoqh
import com.lemon.now.ui.model.OrderViewModel
import com.lemon.now.util.SettingUtil

/**
 *   Lemon Cash
 *  OrderListActivity.java
 *
 */
class OrderListActivity : BaseActivity1<OrderViewModel, ActivityOrderlistBinding>() {
    private val dataList = mutableListOf<VNozeoqh>()
    private var currentPage = 1
    private var isLoading = false
    private var status = 0
    private var falg = false

    private var currentposition = 0
    private val adapter: OrderAdapter by lazy {
        OrderAdapter(object : OrderAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val map = hashMapOf(
                    "eV05C0C38faX7Pe4Gg9yYyGEgYJMSUF9" to SettingUtil.isEmulator().toString(),
                    "KsZ6YErOwuyY5n" to SettingUtil.isVpnConnected(this@OrderListActivity)
                        .toString(),
                    "F9FfkF65u60EyUBta19sf1sT35LncHWhfK" to SettingUtil.isDeviceRooted().toString(),
                    "ty4KsDDtUaDQyufZqE" to SettingUtil.getAvailableSimSlots(this@OrderListActivity)
                        .toString(),
                    "ZpImjX0pSKJ0oVaRmizPrR0NzNrZyqF2" to SettingUtil.getActivatedSimCount(this@OrderListActivity)
                        .toString(),
                    "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to dataList.get(position).sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t.toString()
                )
                currentposition = position
                mViewModel.orderdetail(map)
            }

        }, this, dataList, status)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }


        mViewBind.llAll.setOnClickListener {
            status = 0
            currentPage = 1
            falg = true
            loadNextPage(false)
        }
        mViewBind.llpen.setOnClickListener {
            status = 1
            currentPage = 1
            falg = true
            loadNextPage(false)
        }
        mViewBind.lldis.setOnClickListener {
            status = 2
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
        mViewBind.lltobe.setOnClickListener {
            status = 3
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
        mViewBind.llfail.setOnClickListener {
            status = 5
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
        mViewBind.lldepin.setOnClickListener {
            status = 5
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
        mViewBind.llover.setOnClickListener {
            status = 6
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
        mViewBind.llrepaid.setOnClickListener {
            status = 4
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
//        loadNextPage()

        val layoutManager = LinearLayoutManager(this)
        mViewBind.recyclerView.layoutManager = layoutManager
        mViewBind.recyclerView.adapter = adapter

        mViewBind.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!recyclerView.canScrollVertically(1) && totalItemCount > 0 && lastVisibleItem >= totalItemCount - 1) {
                    loadNextPage(false)
                }
            }
        })
        mViewBind.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            falg = true
            loadNextPage(false)
        }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            showLoading("loading")

        }, 500)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            dismissLoading()

        }, 2000)
    }
    override fun onRestart() {
        super.onRestart()
        currentPage = 1
        falg = true
        loadNextPage(false)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            showLoading("loading")

        }, 500)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            dismissLoading()

        }, 2000)
    }

    private fun loadNextPage(isShowDialog: Boolean) {
        isLoading = true
        val map = hashMapOf(
            "gechsSbDxMXWr0ebMWvxyyMrfgsU6u4CEs" to currentPage,
            "oFAPPnaXGtjaefpW2ahcm" to 10
        )
        if (status != 0) {
            map.put("N4ZFdMcD64dDJ", status)
        }
        mViewModel.orderlist(map,isShowDialog)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            currentPage = 1
            falg = true
            loadNextPage(true)
        }
    }
    override fun createObserver() {

        mViewModel.orderdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (falg) {
                    dataList.clear()
                }
                falg = false
                dataList.addAll(it.vNozeoqh)
                adapter.notifyDataSetChanged()
                currentPage++
                isLoading = false
                mViewBind.swipeRefreshLayout.isRefreshing = false
            }
        })

        mViewModel.orderdetaildata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                SettingUtil.startListActivity( it.M7CdaEiz0WPh1Cs3iyzkg6Od,this)

            }
        })
    }
}