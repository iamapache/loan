package com.lemon.now.ui.activity

import SPUtil
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityMineBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.model.HomeViewModel
import com.lemon.now.ui.model.LoginModel
import com.lemon.now.ui.model.MessageEvent
import com.lemon.now.util.SettingUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *   Lemon Cash
 *  MineActivity.java
 *
 */
class MineActivity : BaseActivity1<LoginModel, ActivityMineBinding>() {
    val viewModel: HomeViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        serview()
        var loginToken: String by SPUtil(ApiService.loginToken, "")
        if (loginToken.isNullOrEmpty()) {
            mViewBind.llbank.setOnClickListener {
                startActivity(Intent(this@MineActivity, LoginActivity::class.java))
            }
        } else {
            mViewBind.llbank.setOnClickListener {
                viewModel.getuserinfo(
                    SettingUtil.isVpnConnected(this@MineActivity).toString(),
                    SettingUtil.getAvailableSimSlots(this@MineActivity).toString(),
                    SettingUtil.getActivatedSimCount(this@MineActivity).toString()
                )

            }
        }

        mViewBind.llaboutus.setOnClickListener {
            startActivity(Intent(this@MineActivity, AboutusActivity::class.java))
        }
        mViewBind.llprivacy.setOnClickListener {
            startActivity(Intent(this@MineActivity, PrivacyActivity::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {
        if (messageEvent.getStatus() == MessageEvent.login) {
            serview()
        }
    }

    override fun createObserver() {

        mViewModel.commonData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {

                var loginToken: String by SPUtil(ApiService.loginToken, "")
                loginToken = ""
                var phone: String by SPUtil(ApiService.phone, "")
                phone = ""
                SPUtil.clearalle()
                var isFirst: Boolean by SPUtil("isFirst", false)
                isFirst = true
                serview()
                startActivity(Intent(this@MineActivity, LoginActivity::class.java))
            }
        })

        viewModel.homebean.observe(this, Observer {
            if (it.mKDa0r8qOCzrAe6Pj == 1) {
                val intent = Intent(this@MineActivity, AuthenticationActivity::class.java)
                startActivity(intent)

            } else if (it.mKDa0r8qOCzrAe6Pj == 2) {
                startActivity(Intent(this@MineActivity, BankActivity::class.java))
            }
        })
    }

    private fun serview() {
        var loginToken: String by SPUtil(ApiService.loginToken, "")
        if (loginToken.isNullOrEmpty()) {
            mViewBind.minelogo.setBackgroundResource(R.mipmap.yuanlogo)
            mViewBind.logout.visibility = View.GONE
            mViewBind.login.text = "Please log in"
            mViewBind.login.setOnClickListener {
                startActivity(
                    Intent(
                        this@MineActivity,
                        LoginActivity::class.java
                    )
                )
            }
        } else {
            mViewBind.minelogo.setBackgroundResource(R.mipmap.aulogo)
            mViewBind.logout.visibility = View.VISIBLE
            mViewBind.logout.setOnClickListener { mViewModel.logout() }
            var phone: String by SPUtil(ApiService.phone, "")
            mViewBind.login.text = phone
        }
    }
}