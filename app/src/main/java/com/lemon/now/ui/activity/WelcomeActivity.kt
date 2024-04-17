package com.lemon.now.ui.activity

import SPUtil
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.adjust.sdk.Adjust
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.online.databinding.ActivityWelcomeBinding
import java.util.UUID


/**
 *   Lemon Cash
 *  WelcomeActivity.java
 *
 */
class WelcomeActivity : BaseActivity1<BaseViewModel, ActivityWelcomeBinding>() {



    override fun initView(savedInstanceState: Bundle?) {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
        val lp = this.window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.attributes = lp
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        getInstallReferrer(this)
        Adjust.getGoogleAdId(this) { googleadid->
            var googleAdid:   String by SPUtil("googleadid", UUID.randomUUID().toString())
            googleAdid=googleadid.toString()?:""
        }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
             val isFirst:   Boolean by SPUtil("isFirst", false)
        if (isFirst){
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        }else{
             startActivity(Intent(this@WelcomeActivity, SplashActivity::class.java))
        }
             finish()
        }, 3000)
    }
    private fun getInstallReferrer(applicationContext: Context) {
        val installReferrerClient = InstallReferrerClient.newBuilder(applicationContext)
            .build()

        installReferrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response: ReferrerDetails = installReferrerClient.installReferrer
                        val referrerUrl: String = response.installReferrer
                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
                        Adjust.setReferrer(referrerUrl, applicationContext)
                        installReferrerClient.endConnection()
                        var referrerurl:   String by SPUtil("referrerurl", "")
                        referrerurl=referrerUrl
                    }
                    // ...
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }
}