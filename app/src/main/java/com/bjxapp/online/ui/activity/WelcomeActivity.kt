package com.bjxapp.online.ui.activity

import SPUtil
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.bjxapp.online.base.activity.BaseActivity
import com.bjxapp.online.base.viewmodel.BaseViewModel
import com.lemon.now.online.databinding.ActivityWelcomeBinding


/**
 *   Lemon Cash
 *  WelcomeActivity.java
 *
 */
class WelcomeActivity : BaseActivity<BaseViewModel, ActivityWelcomeBinding>() {



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

}