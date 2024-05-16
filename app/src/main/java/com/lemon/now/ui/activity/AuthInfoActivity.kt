package com.lemon.now.ui.activity

import ToastUtils
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.lemon.now.base.activity.BaseActivity
import com.lemon.now.online.databinding.ActivityAuthinfoBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.bean.Userbean
import com.lemon.now.ui.model.AuthModel
import com.lemon.now.ui.model.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 *   Lemon Cash
 *  AuthenticationActivity.java
 *
 */
class AuthInfoActivity : BaseActivity<AuthModel, ActivityAuthinfoBinding>() {
    private var male: String = ""
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.back.setOnClickListener {
            finish()
        }
        mViewModel.authinfo()
        mDatabind.cbmale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mDatabind.cbfemale.isChecked = false
                male = mDatabind.cbmale.text.toString()
            } else {
                male = ""
            }
        }

        mDatabind.cbfemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mDatabind.cbmale.isChecked = false
                male = mDatabind.cbfemale.text.toString()
            } else {
                male = ""
            }
        }
        var userbean = intent.getParcelableExtra<Userbean>("userbean")
        if (userbean != null) {
            mDatabind.address.setText(userbean.address)
            mDatabind.name.setText(userbean.userNames)
            mDatabind.Date.setText(userbean.date)
            mDatabind.number.setText(userbean.userNumber)
            mDatabind.pannumber.setText(userbean.panNumber)
            if (userbean.male.equals("male")) {
                mDatabind.cbmale.isChecked=true
            } else if (userbean.male.equals("female")) {
                mDatabind.cbfemale.isChecked =true
            }
        }


        mDatabind.llDate.setOnClickListener {
            showDatePickerDialog()
        }
        mDatabind.next.setOnClickListener {
            val adjustEvent = AdjustEvent(ApiService.step2)
            Adjust.trackEvent(adjustEvent)
            if (mDatabind.name.text.toString().isNotEmpty() && mDatabind.number.text.toString()
                    .isNotEmpty()
                && mDatabind.Date.text.toString().isNotEmpty() && male.isNotEmpty()
                && mDatabind.address.text.toString()
                    .isNotEmpty() && mDatabind.pannumber.text.toString().isNotEmpty()
                && mDatabind.accountnumber.text.toString()
                    .isNotEmpty() && mDatabind.bankname.text.toString().isNotEmpty()
                && mDatabind.ifsc.text.toString().isNotEmpty()
            ) {

                val map = hashMapOf(
                    "ZbspMUi4YGmGq0ioNH6mrzQcHCsGrz883Ed" to "2",
                    "JagPyb82zgGT3vxOUXzqsvYoli410PMB" to userbean?.frontimg!!,
                    "aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9" to userbean?.backimg!!,
                    "RVdeam" to mDatabind.name.text.toString(),
                    "QEwbbq7EWftpSMsC8ACVRbpKmLb" to mDatabind.number.text.toString(),
                    "j3go0Or3oSXdLKCVoXSWqH4YFDlGxfeuF5SR" to mDatabind.Date.text.toString(),
                    "ByBVgtNcR1nQgAIykVh9srO0b2XMFzj" to male,
                    "QI7zJuHhm40svgmoacMA41EyG7Onn" to mDatabind.bankname.text.toString(),
                    "DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh" to mDatabind.accountnumber.text.toString(),
                    "NfdUWBoWLpCLqyf83El" to mDatabind.ifsc.text.toString(),
                    "parSQe8ddYXQh" to userbean?.cardImg!!,
                    "UeFqjoQ5IDuu8B3Zabq0" to mDatabind.pannumber.text.toString(),
                    "libETpAU" to mDatabind.address.text.toString()
                )
                mViewModel.submitAuth(map)
            } else {
                ToastUtils.showShort(this@AuthInfoActivity, "Please fill in all the information")
            }

        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {
        if (messageEvent.getStatus() == MessageEvent.finish) {
            finish()
        }
    }
    override fun createObserver() {

        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val intent = intent
                intent.setClass(this@AuthInfoActivity, AuthInfoDetailActivity::class.java)
                startActivity(intent)
            } else {
                ToastUtils.showShort(this@AuthInfoActivity, it.vWCgp64OkxPVoGqics)
            }
        })

        mViewModel.authinfoData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                mDatabind.cbmale.text = it.NTxW7IvON[0]
                mDatabind.cbfemale.text = it.NTxW7IvON[1]

                mDatabind.accountnumber.setText( it.DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh.toString())
                mDatabind.bankname.setText( it.QI7zJuHhm40svgmoacMA41EyG7Onn.toString())
                mDatabind.ifsc.setText( it.NfdUWBoWLpCLqyf83El.toString())
            }
        })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate: String = sdf.format(calendar.time)
                mDatabind.Date.setText(formattedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }
}