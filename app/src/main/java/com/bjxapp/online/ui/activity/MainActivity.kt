package com.bjxapp.online.ui.activity

import android.content.Intent
import android.os.Bundle
import com.bjxapp.online.base.activity.BaseActivity
import com.bjxapp.online.base.etx.util.setTitleCenter
import com.bjxapp.online.ui.model.HomeViewModel
import com.lemon.now.online.databinding.ActivityMainBinding


class MainActivity : BaseActivity<HomeViewModel, ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.toolbar.setTitleCenter("Home")
        mDatabind.llOrder.setOnClickListener { startActivity(Intent(this@MainActivity, LoginActivity::class.java)) }

        mViewModel.post()
    }

}
