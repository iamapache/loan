package com.lemon.now.ui.activity

import ToastUtils
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.view.CustomDialog
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityBankinfoBinding
import com.lemon.now.ui.model.AuthModel
import com.lemon.now.ui.model.MessageEvent
import com.lemon.now.util.SettingUtil
import org.greenrobot.eventbus.EventBus

/**
 *   Lemon Cash
 *  BankActivity.java
 *
 */
class BankInfoActivity : BaseActivity1<AuthModel, ActivityBankinfoBinding>() {
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        someActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                                    mViewModel.getuserinfo(
                    SettingUtil.isVpnConnected(this@BankInfoActivity).toString(),SettingUtil.getAvailableSimSlots(this@BankInfoActivity).toString(),
                    SettingUtil.getActivatedSimCount(this@BankInfoActivity).toString()
                )
                }
            }
        mViewBind.next.setOnClickListener {

            when {
                mViewBind.accountnumber.text.toString().isEmpty() -> ToastUtils.showShort(
                    this@BankInfoActivity,
                    getString(R.string.toastinformation)
                )

                mViewBind.bankname.text.toString().isEmpty() -> ToastUtils.showShort(
                    this@BankInfoActivity,
                    getString(R.string.toastinformation)
                )

                mViewBind.ifsc.text.toString().isEmpty() -> ToastUtils.showShort(
                    this@BankInfoActivity,
                    getString(R.string.toastinformation)
                )

                else -> {
                    val dialog = CustomDialog(this@BankInfoActivity)
                    dialog.setCancelable(false)
                    dialog.setConfirmCallback {


                        val map = hashMapOf(
                            "ZbspMUi4YGmGq0ioNH6mrzQcHCsGrz883Ed" to "5",
                            "QI7zJuHhm40svgmoacMA41EyG7Onn" to mViewBind.bankname.text.toString(),
                            "DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh" to mViewBind.accountnumber.text.toString(),
                            "NfdUWBoWLpCLqyf83El" to mViewBind.ifsc.text.toString()
                        )
                        mViewModel.submitAuth(map)
                    }
                    dialog.setCancelCallback {

                    }
                    dialog.show()
                    dialog.setTitle("TIPS")
                    dialog.setcontent("Please note: Apart from bank card details, no other information can be modified once submitted. Ensure all information is accurate before proceeding with your submission.")
                }
            }
        }
    }

    override fun createObserver() {

        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                EventBus.getDefault().post(MessageEvent(MessageEvent.au,""))
                val dialog = CustomDialog(this@BankInfoActivity)
                dialog.setConfirmCallback {
                    val intent = Intent(this@BankInfoActivity, UserFaceActivity::class.java)
                    intent.putExtra("phototext", "")
                    someActivityResultLauncher.launch(intent)
                }
                dialog.setCancelCallback {

                }
                dialog.setCancelable(false)
                dialog.show()
                dialog.setTitle("TIPS")
                dialog.setcontent("Please upload a selfie photo before continuing.")


            } else {
                ToastUtils.showShort(this@BankInfoActivity, it.vWCgp64OkxPVoGqics)
            }
        })

        mViewModel.homebean.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (it.Qbnsde5LgABnpY9IpFTFXkgR3l8!=null){
                    val intent = Intent(this@BankInfoActivity, OrderActivity::class.java)
                    intent.putExtra("id", it.Qbnsde5LgABnpY9IpFTFXkgR3l8.nJNb2VY6)
                    startActivity(intent)

                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }else {
                ToastUtils.showShort(this@BankInfoActivity, it.vWCgp64OkxPVoGqics)
            }
        })
    }
}