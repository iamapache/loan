package com.lemon.now.base.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.lemon.now.base.etx.util.bindingWithGeneric
import com.lemon.now.base.viewmodel.BaseViewModel

abstract class BaseBindActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mViewBind: VB

    override fun initDataBind(): View? {
        mViewBind = bindingWithGeneric(layoutInflater)
        return mViewBind.root

    }
}