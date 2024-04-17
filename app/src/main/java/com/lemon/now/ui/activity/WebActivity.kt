package com.lemon.now.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.online.databinding.ActivityWebBinding

/**
 *   Lemon Cash
 *  WebActivity.java
 *
 */
class WebActivity  : BaseActivity1<BaseViewModel, ActivityWebBinding>() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        val param1 = intent.getStringExtra("url")

        mViewBind.web.loadUrl(param1.toString())
        mViewBind.web.settings.javaScriptEnabled = true
    }
}