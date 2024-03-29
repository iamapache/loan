package com.bjxapp.online.ui.activity

import android.os.Bundle
import com.bjxapp.online.base.activity.BaseActivity1
import com.bjxapp.online.base.viewmodel.BaseViewModel
import com.bjxapp.online.ui.adapter.MainAdapter
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityMainBinding


class MainActivity : BaseActivity1<BaseViewModel, ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.mainViewPager.adapter = MainAdapter(this)
        mViewBind.mainViewPager.offscreenPageLimit = mViewBind.mainViewPager.adapter!!.itemCount
        mViewBind.mainNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigationHome -> {
                    //切换到首页
                    mViewBind.mainViewPager.setCurrentItem(0, false)
                }
                R.id.navigationUser -> {
                    //切换到个人中心
                    mViewBind.mainViewPager.setCurrentItem(3, false)
                }
            }
            true
        }
    }

}
