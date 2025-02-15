package com.lemon.now.ui.activity

import ToastUtils
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityBankBinding
import com.lemon.now.ui.model.AuthModel
import com.lemon.now.ui.model.HomeViewModel

/**
 *   Lemon Cash
 *  BankActivity.java
 *
 */
class BankActivity : BaseActivity1<HomeViewModel, ActivityBankBinding>() {
    val viewModel: AuthModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }

        mViewBind.next.setOnClickListener {
            when {
                mViewBind.account.text.toString().isEmpty() -> ToastUtils.showShort(
                    this@BankActivity, getString(
                        R.string.toastinformation
                    )
                )

                mViewBind.name.text.toString().isEmpty() -> ToastUtils.showShort(
                    this@BankActivity,
                    getString(R.string.toastinformation)
                )

                mViewBind.code.text.toString().isEmpty() -> ToastUtils.showShort(
                    this@BankActivity,
                    getString(R.string.toastinformation)
                )

                else -> mViewModel.bank(
                    mViewBind.account.text.toString(),
                    mViewBind.name.text.toString(),
                    mViewBind.code.text.toString()
                )
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            showLoading("loading")
            viewModel.authinfo()
        }, 500)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            dismissLoading()

        }, 2000)
    }
    override fun onRestart() {
        super.onRestart()


    }
    override fun createObserver() {

        mViewModel.bankdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                ToastUtils.showShort(this@BankActivity, it.vWCgp64OkxPVoGqics)
                finish()
            } else {
                ToastUtils.showShort(this@BankActivity, it.vWCgp64OkxPVoGqics)
            }
        })

        viewModel.authinfoData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                mViewBind.account.setText(it.DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh)
                mViewBind.name.setText(it.QI7zJuHhm40svgmoacMA41EyG7Onn)
                mViewBind.code.setText(it.NfdUWBoWLpCLqyf83El)
            }
        })
    }
}