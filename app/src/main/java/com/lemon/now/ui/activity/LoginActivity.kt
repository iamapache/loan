package com.lemon.now.ui.activity

import SPUtil
import ToastUtils
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.lemon.now.base.activity.BaseActivity
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityLoginBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.model.LoginModel
import com.lemon.now.ui.model.MessageEvent
import com.lemon.now.util.SettingUtil
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit


/**
 *   Lemon Cash
 *  LoginActivity.java
 *
 */
class LoginActivity : BaseActivity<LoginModel, ActivityLoginBinding>() {
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 0
    private val COUNTDOWN_TIME: Long = 60000
    private val INTERVAL: Long = 1000

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        mDatabind.click = ProxyClick()

        val style = SpannableStringBuilder("By continuing you agree our Terms & Conditions and Privacy Policy.")
        style.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
            28,
            46,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        style.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@LoginActivity, WebActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("param1", "value1")
                startActivity(intent)
            }

        }, 28, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(this@LoginActivity,R.color.colorPrimary)
            ),
            51,
            65,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        style.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@LoginActivity, WebActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("param1", "value1")
                startActivity(intent)
            }
        }, 51, 65, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mDatabind.agree.movementMethod = LinkMovementMethod.getInstance()
        mDatabind.agree.highlightColor = resources.getColor(android.R.color.transparent)
        mDatabind.agree.text = style

        if(intent.hasExtra("msg")){
            ToastUtils.showShort(this,"Login expire")
        }
    }

    inner class ProxyClick {

        fun gofinish() {
            finish()
        }

        fun login() {
            when {
                mViewModel.username.get().isEmpty() -> ToastUtils.showShort(this@LoginActivity,"Please enter a 10-digit mobile number")
                mViewModel.editText.get().isEmpty() -> ToastUtils.showShort(this@LoginActivity,"Please enter a code")
                mViewModel.editText2.get().isEmpty() -> ToastUtils.showShort(this@LoginActivity,"Please enter a code")
                mViewModel.editText3.get().isEmpty() -> ToastUtils.showShort(this@LoginActivity,"Please enter a code")
                mViewModel.editText4.get().isEmpty() -> ToastUtils.showShort(this@LoginActivity,"Please enter a code")
                !mDatabind.agreeCheckBox.isChecked-> ToastUtils.showShort(this@LoginActivity,"Please agree with our policy to continue")
                else -> mViewModel.post(
                    mViewModel.username.get(),
                    mViewModel.editText.get()+mViewModel.editText2.get()+mViewModel.editText3.get()+mViewModel.editText4.get(),
                    SettingUtil.isVpnConnected(this@LoginActivity).toString(),SettingUtil.getAvailableSimSlots(this@LoginActivity).toString(),
                    SettingUtil.getActivatedSimCount(this@LoginActivity).toString()
                )
            }
        }

        fun getotp() {

            when {
                mViewModel.username.get().isEmpty() -> ToastUtils.showShort(this@LoginActivity,"Please enter a 10-digit mobile number")
                else -> mViewModel.code(
                    mViewModel.username.get(),)
            }
        }

    }
    override fun createObserver() {
        mViewModel.result.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                var loginToken:   String by SPUtil(ApiService.loginToken, "")
                loginToken=it.CtEDjA
                var phone:   String by SPUtil(ApiService.phone, "")
                phone=mViewModel.username.get()
                EventBus.getDefault().post(MessageEvent(MessageEvent.login,""))
                finish()
            }else{
                ToastUtils.showShort(this@LoginActivity,it.vWCgp64OkxPVoGqics)
            }
        })

        mViewModel.commonData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                startCountdown()
                ToastUtils.showShort(this@LoginActivity,it.vWCgp64OkxPVoGqics)
            }else{
                ToastUtils.showShort(this@LoginActivity,it.vWCgp64OkxPVoGqics)
            }
        })
    }
    private fun startCountdown() {
        timeLeftInMillis = COUNTDOWN_TIME
        countDownTimer = object : CountDownTimer(timeLeftInMillis, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountdownText()
                mDatabind.getotp.setBackgroundResource(R.drawable.button_shapgary)
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateCountdownText()
                mDatabind.getotp.setBackgroundResource(R.drawable.button_shap)
                mDatabind.getotp.text = "Get OTP"

            }
        }.start()
    }

    private fun updateCountdownText() {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) -
                TimeUnit.MINUTES.toSeconds(minutes)
        val timeFormatted = String.format("%02d"+" s", seconds)
        mDatabind.getotp.text = timeFormatted

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}