package com.lemon.now.ui.activity

import SPUtil
import ToastUtils
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.view.LoanDialog
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityMainBinding
import com.lemon.now.ui.ApiService.Companion.loginToken
import com.lemon.now.ui.bean.O2Hvk1wvAGN
import com.lemon.now.ui.model.HomeViewModel
import com.lemon.now.ui.model.MessageEvent
import com.lemon.now.util.SettingUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.random.Random


class MainActivity : BaseActivity1<HomeViewModel, ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        var loginToken: String by SPUtil(loginToken, "")
        mViewBind.llOrder.setOnClickListener {
            if (loginToken.isNullOrEmpty()) {
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
            } else {
                startActivity(
                    Intent(
                        this@MainActivity,
                        OrderListActivity::class.java
                    )
                )
            }
        }
        mViewBind.llfb.setOnClickListener {
            if (loginToken.isNullOrEmpty()) {
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
            } else {
                startActivity(
                    Intent(
                        this@MainActivity,
                        FabackListActivity::class.java
                    )
                )
            }
        }
        mViewBind.llMine.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    MineActivity::class.java
                )
            )
        }
        getHomeData()
        mViewBind.swipeRefreshLayout.setOnRefreshListener {
            getHomeData()
        }
        mViewModel.firsttime()

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {
        if (messageEvent.getStatus() == MessageEvent.login) {
            getHomeData()
        } else if (messageEvent.getStatus() == MessageEvent.au) {
            mViewModel.getuserinfo(SettingUtil.isVpnConnected(this@MainActivity).toString(),SettingUtil.getAvailableSimSlots(this@MainActivity).toString(),
                SettingUtil.getActivatedSimCount(this@MainActivity).toString())
        }
    }

    private fun getHomeData() {
        var loginToken: String by SPUtil(loginToken, "")
        if (loginToken.isNullOrEmpty()) {
            mViewModel.homelist()
        } else {
            mViewModel.getuserinfo(
                SettingUtil.isVpnConnected(this@MainActivity).toString(),
                SettingUtil.getAvailableSimSlots(this@MainActivity).toString(),
                SettingUtil.getActivatedSimCount(this@MainActivity).toString()
            )
        }
    }

    var productdd = ""
    override fun createObserver() {
        mViewModel.result.observe(this, Observer {
            mViewBind.llBg.setBackgroundResource(R.mipmap.homebg)
            mViewBind.hometop.setBackgroundResource(R.mipmap.hometop)
            updata(it.o2Hvk1wvAGN[0])
            mViewBind.layoutProduct.fendata.text = "1/1"
            mViewBind.layoutProduct.profileBack.setOnClickListener {

            }
            mViewBind.swipeRefreshLayout.isRefreshing = false
            mViewBind.layoutProduct.profileNext.setOnClickListener {
            }
            mViewBind.layoutProduct.next.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
            }
        })
        var current = 0
        mViewModel.homebean.observe(this, Observer {
            mViewBind.swipeRefreshLayout.isRefreshing = false
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {

                var homebean = it
                updata(it.o2Hvk1wvAGN[0])
                productdd = it.o2Hvk1wvAGN[0].nJNb2VY6
                mViewBind.layoutProduct.fendata.text = (current + 1).toString() + "/" + homebean.PLnVmUR
                mViewBind.layoutProduct.profileBack.setOnClickListener {
                    if (current > 0 && current <= homebean.PLnVmUR - 1) {
                        current--
                        mViewBind.layoutProduct.fendata.text =
                            (current + 1).toString() + "/" + homebean.PLnVmUR
                        updata(homebean.o2Hvk1wvAGN[current])
                    }

                }
                mViewBind.layoutProduct.profileNext.setOnClickListener {
                    if (current >= 0 && current < homebean.PLnVmUR - 1) {
                        current++
                        mViewBind.layoutProduct.fendata.text =
                            (current + 1).toString() + "/" + homebean.PLnVmUR
                        updata(homebean.o2Hvk1wvAGN[current])
                    }
                }


                if (it.RCozitsRJKeDOemhQB == 1) {
                    val dialog = LoanDialog(this@MainActivity)
                    dialog.setConfirmCallback {
                    }
                    dialog.setCancelCallback {
                    }
                    dialog.show()
                    dialog.setTitle(it.Ni3ovnEH3M4SxVAQq0qcKdnn)
                    dialog.setcontent(it.STfcvbTWCpQuwrJ20nwGJ)
                    dialog.setimg(it.Q7YKhpm77TSWYpeHBXYFn1feV)
                    dialog.setloannotext(it.jvytkVyzR5Qe9Y8K31jmzxdUZpImQJ)
                }
                if (it.mKDa0r8qOCzrAe6Pj == 1) {
                    val intent = Intent(this@MainActivity, AuthenticationActivity::class.java)
                    startActivity(intent)
                    mViewBind.llBg.setBackgroundResource(R.mipmap.homebg)
                    mViewBind.hometop.setBackgroundResource(R.mipmap.hometop)

                } else if (it.mKDa0r8qOCzrAe6Pj == 2) {
                    mViewBind.llBg.setBackgroundResource(R.mipmap.auhomebg)
                    mViewBind.hometop.setBackgroundResource(R.mipmap.auhometop)
                }
                mViewBind.layoutProduct.next.setOnClickListener {
                    mViewModel.checkorderstatus(productdd,SettingUtil.isVpnConnected(this@MainActivity).toString(),SettingUtil.getAvailableSimSlots(this@MainActivity).toString(),
                        SettingUtil.getActivatedSimCount(this@MainActivity).toString())
                }
            }
        })

        mViewModel.orderstatusdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                when (it.nQvJKMbHsO5F) {
                    1 -> {
                        val intent = Intent(this@MainActivity, AuthenticationActivity::class.java)
                        startActivity(intent)

                    }
                    2 -> {
                        val intent = Intent(this@MainActivity, OrderActivity::class.java)
                        intent.putExtra("id", productdd)
                        startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(this@MainActivity, OrderListActivity::class.java)
                        startActivity(intent)
                    }
                }
            }else {
                ToastUtils.showShort(this@MainActivity, it.vWCgp64OkxPVoGqics)
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
