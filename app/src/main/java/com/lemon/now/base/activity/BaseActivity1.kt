package com.lemon.now.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.lemon.now.base.etx.view.dismissLoadingExt
import com.lemon.now.base.etx.view.showLoadingExt
import com.lemon.now.base.viewmodel.BaseViewModel

abstract class BaseActivity1<VM : BaseViewModel, VB : ViewBinding> : BaseBindActivity<VM, VB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun createObserver() {}

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }
}