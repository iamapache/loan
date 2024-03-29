package com.bjxapp.online.base.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.bjxapp.online.base.etx.util.inflateBindingWithGeneric
import com.bjxapp.online.base.viewmodel.BaseViewModel

/**
 * 项目：  Lemon Cash
 * 类名：  BaseVmVbActivity.java
 * 时间：  2024/3/28 0028 19:23
 * 描述：
 */
abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mViewBind: VB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root

    }
}