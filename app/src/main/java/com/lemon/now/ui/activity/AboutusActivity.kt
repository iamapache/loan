package com.lemon.now.ui.activity

import android.os.Bundle
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.BuildConfig
import com.lemon.now.online.databinding.ActivityAboutsBinding
import com.lemon.now.ui.model.HomeViewModel
/**
 *   Lemon Cash
 *  AboutusActivity.java
 *
 */
class AboutusActivity : BaseActivity1<HomeViewModel, ActivityAboutsBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.version.text="Version "+BuildConfig.VERSION_CODE.toString()
    }
}