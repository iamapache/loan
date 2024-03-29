package com.bjxapp.online.base.fragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.bjxapp.online.base.etx.util.hideSoftKeyboard
import com.bjxapp.online.base.etx.view.dismissLoadingExt
import com.bjxapp.online.base.etx.view.showLoadingExt
import com.bjxapp.online.base.viewmodel.BaseViewModel

/**
 * 项目：  Lemon Cash
 * 类名：  BaseFragment1.java
 * 描述：
 */
abstract class BaseFragment1<VM : BaseViewModel, VB : ViewBinding> : BaseVmVbFragment<VM, VB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    override fun lazyLoadData() {}

    /**
     * 创建LiveData观察者 Fragment执行onViewCreated后触发
     */
    override fun createObserver() {}

    /**
     * Fragment执行onViewCreated后触发
     */
    override fun initData() {

    }

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

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    override fun lazyLoadTime(): Long {
        return 300
    }
}