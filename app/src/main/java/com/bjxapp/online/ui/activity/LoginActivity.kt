package com.bjxapp.online.ui.activity

import android.os.Bundle
import com.bjxapp.online.base.activity.BaseActivity
import com.bjxapp.online.ui.model.LoginRegisterViewModel
import com.lemon.now.online.databinding.ActivityLoginBinding

/**
 *   Lemon Cash
 *  LoginActivity.java
 *
 */
class LoginActivity : BaseActivity<LoginRegisterViewModel, ActivityLoginBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        mDatabind.click = ProxyClick()
    }

    inner class ProxyClick {

        fun clear() {
            mViewModel.username.set("")
        }

        fun login() {
            when {
                else -> mViewModel.post(
                    mViewModel.username.get(),
                    mViewModel.password.get()
                )
            }
        }

        fun goRegister() {
        }

    }
}