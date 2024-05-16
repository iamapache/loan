package com.lemon.now.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.lemon.now.base.etx.view.dismissLoadingExt
import com.lemon.now.base.etx.view.dismissLoadingExt2
import com.lemon.now.base.etx.view.showLoadingExt
import com.lemon.now.base.viewmodel.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseViewActivity<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun createObserver() {}

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
        dismissLoadingExt2()
    }

}