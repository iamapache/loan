package com.lemon.now.ui.activity

import android.content.Intent
import android.os.Bundle
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

            override fun onFBClick(position: Int) {
                val intent = Intent(this@OrderListActivity, FabackActivity::class.java)
                intent.putExtra("img", dataList.get(position).pPQGUTNAxoA)
                intent.putExtra("name", dataList.get(position).zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV)
                intent.putExtra("id", dataList.get(position).sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t)
                startActivity(intent)
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
            loadNextPage()
        }
        mViewBind.llpen.setOnClickListener {
            status = 1
            currentPage = 1
            falg = true
            loadNextPage()
        }
        mViewBind.lldis.setOnClickListener {
            status = 2
            currentPage = 1
            falg = true
            loadNextPage()
        }
        mViewBind.lltobe.setOnClickListener {
            status = 3
            currentPage = 1
            falg = true
            loadNextPage()
        }
        mViewBind.llfail.setOnClickListener {
            status = 5
            currentPage = 1
            falg = true
            loadNextPage()
        }
        mViewBind.lldepin.setOnClickListener {
            status = 5
            currentPage = 1
            falg = true
            loadNextPage()
        }
        mViewBind.llover.setOnClickListener {
            status = 6
            currentPage = 1
            falg = true
            loadNextPage()
        }
        mViewBind.llrepaid.setOnClickListener {
            status = 4
            currentPage = 1
            falg = true
            loadNextPage()
        }
//        loadNextPage()

        val layoutManager = LinearLayoutManager(this)
        mViewBind.recyclerView.layoutManager = layoutManager
        mViewBind.recyclerView.adapter = adapter

        mViewBind.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadNextPage()
                }
            }
        })
        mViewBind.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            falg = true
            loadNextPage()
        }
    }

    private fun loadNextPage() {
        isLoading = true
        val map = hashMapOf(
            "gechsSbDxMXWr0ebMWvxyyMrfgsU6u4CEs" to currentPage,
            "oFAPPnaXGtjaefpW2ahcm" to 10
        )
        if (status != 0) {
            map.put("N4ZFdMcD64dDJ", status)
        }
        mViewModel.orderlist(map,false)
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
                when (it.M7CdaEiz0WPh1Cs3iyzkg6Od.S1giqNNoozNFE8yRzARpD2VdSEkMd) {
                    1 -> {
                        val intent =
                            Intent(this@OrderListActivity, OrderPendingActivity::class.java)
                        intent.putExtra(
                            "titlecontent",
                            "Your loan application has been received, and we will inform you of the outcome as soon as possible."
                        )
                        intent.putExtra("title", "Pending")
                        intent.putExtra("type", 1)
                        intent.putExtra("bean", it.M7CdaEiz0WPh1Cs3iyzkg6Od)
                        startActivity(intent)
                    }

                    2 -> {
                        val intent =
                            Intent(this@OrderListActivity, OrderPendingActivity::class.java)
                        intent.putExtra(
                            "titlecontent",
                            "Your loan is currently being disbursed. We will notify you once the disbursement is complete."
                        )
                        intent.putExtra("title", "Disbursing")
                        intent.putExtra("type", 2)
                        intent.putExtra("bean", it.M7CdaEiz0WPh1Cs3iyzkg6Od)
                        startActivity(intent)
                    }
                    3 -> {
                        val intent =
                            Intent(this@OrderListActivity, OrderTobeActivity::class.java)
                        intent.putExtra("title", "To be Repaid")
                        intent.putExtra("type", 3)
                        intent.putExtra("bean", it.M7CdaEiz0WPh1Cs3iyzkg6Od)
                        startActivity(intent)
                    }
                    4 -> {
                        val intent =
                            Intent(this@OrderListActivity, OrderRepaidActivity::class.java)
                        intent.putExtra("title", "Repaid")
                        if(it.M7CdaEiz0WPh1Cs3iyzkg6Od.nMPtLLw7ysMUUZ3W6clfFgpCS9C?.toInt()?:0 >0){
                            intent.putExtra("type", 1)
                        }else{
                            intent.putExtra("type", 0)
                        }
                        intent.putExtra("bean", it.M7CdaEiz0WPh1Cs3iyzkg6Od)
                        startActivity(intent)
                    }
                    5 -> {
                        val intent =
                            Intent(this@OrderListActivity, OrderPendingActivity::class.java)

                        if(it.M7CdaEiz0WPh1Cs3iyzkg6Od.a2kevgH5EWY9waHNv76F6xKXEwY==1){
                            intent.putExtra(
                                "titlecontent",
                                "Please ensure your bank information is accurate and submit application again."
                            )
                            intent.putExtra("title", "Disbursing Fail")
                            intent.putExtra("type", 5)
                        }else if(it.M7CdaEiz0WPh1Cs3iyzkg6Od.a2kevgH5EWY9waHNv76F6xKXEwY==0){
                            if(it.M7CdaEiz0WPh1Cs3iyzkg6Od.nMPtLLw7ysMUUZ3W6clfFgpCS9C?.toInt()?:0 >0){
                                intent.putExtra(
                                    "titlecontent",
                                    "Unfortunately, your loan application was not approved. You may reapply for this product after 6 days, or consider applying for a different product immediately."
                                )
                                intent.putExtra("type", 555)
                                intent.putExtra("title", "Detail")
                            }else{
                                intent.putExtra(
                                    "titlecontent",
                                    "Hello, you are now ready to proceed with this loan application."
                                )
                                intent.putExtra("type", 55)
                                intent.putExtra("title", "Denied")
                            }
                        }
                        intent.putExtra("bean", it.M7CdaEiz0WPh1Cs3iyzkg6Od)
                        startActivity(intent)
                    }

                    6 -> {
                        val intent =
                            Intent(this@OrderListActivity, OrderTobeActivity::class.java)
                        intent.putExtra("title", "Overdue")
                        intent.putExtra("type", 6)
                        intent.putExtra("bean", it.M7CdaEiz0WPh1Cs3iyzkg6Od)
                        startActivity(intent)
                    }
                }

            }
        })
    }
}