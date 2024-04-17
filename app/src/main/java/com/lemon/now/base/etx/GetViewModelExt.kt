package com.lemon.now.base.etx

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lemon.now.base.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType


@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

inline fun <reified VM : BaseViewModel> AppCompatActivity.getAppViewModel(): VM {
    (this.application as? BaseApp).let {
        if (it == null) {
            throw NullPointerException("getAppViewModel该方法")
        } else {
            return it.getAppViewModelProvider().get(VM::class.java)
        }
    }
}

inline fun <reified VM : BaseViewModel> Fragment.getAppViewModel(): VM {
    (this.requireActivity().application as? BaseApp).let {
        if (it == null) {
            throw NullPointerException("getAppViewModel该方法")
        } else {
            return it.getAppViewModelProvider().get(VM::class.java)
        }
    }
}

inline fun <reified VM : BaseViewModel> AppCompatActivity.getViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(application)
    ).get(VM::class.java)
}

inline fun <reified VM : BaseViewModel> Fragment.getViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(VM::class.java)
}

inline fun <reified VM : BaseViewModel> Fragment.getActivityViewModel(): VM {
    return ViewModelProvider(requireActivity(),
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(VM::class.java)
}






