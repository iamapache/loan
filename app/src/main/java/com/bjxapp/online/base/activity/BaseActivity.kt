package com.bjxapp.online.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.bjxapp.online.base.etx.view.dismissLoadingExt
import com.bjxapp.online.base.etx.view.showLoadingExt
import com.bjxapp.online.base.viewmodel.BaseViewModel

/**
 * 项目：  Lemon Cash
 * 类名：  BaseActivity.java
 * 描述：
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

}