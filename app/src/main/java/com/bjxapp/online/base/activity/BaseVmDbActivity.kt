package com.bjxapp.online.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.bjxapp.online.base.etx.util.inflateBindingWithGeneric
import com.bjxapp.online.base.viewmodel.BaseViewModel

/**
 * 项目：  Lemon Cash
 * 类名：  BaseVmDbActivity.java
 * 时间：  2024/3/28 0028 19:15
 * 描述：
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun layoutId() = 0

    lateinit var mDatabind: DB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}