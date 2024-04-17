package com.lemon.now.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.lemon.now.base.etx.util.bindingWithGeneric
import com.lemon.now.base.viewmodel.BaseViewModel

abstract class BaseViewActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun layoutId() = 0

    lateinit var mDatabind: DB

    override fun initDataBind(): View? {
        mDatabind = bindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}