package com.bjxapp.online.ui.activity

import ImageSliderAdapter
import SPUtil
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.bjxapp.online.base.activity.BaseActivity1
import com.bjxapp.online.base.viewmodel.BaseViewModel
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity1<BaseViewModel, ActivitySplashBinding>() {

    private lateinit var viewPager: ViewPager2
    private lateinit var nextButton: Button

    override fun initView(savedInstanceState: Bundle?) {
        val lp = this.window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.attributes = lp
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        var isFirst:   Boolean by SPUtil("isFirst", false)
        isFirst=true
        viewPager = findViewById(R.id.viewPager)
        nextButton = findViewById(R.id.nextButton)

        val images = listOf(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background)
        val adapter = ImageSliderAdapter(images)
        viewPager.adapter = adapter

        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem==2){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                viewPager.setCurrentItem((currentItem + 1) , true)
            }

        }
    }

}