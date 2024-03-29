package com.bjxapp.online.ui.activity

import ImageSliderAdapter
import SPUtil
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.bjxapp.online.base.activity.BaseActivity1
import com.bjxapp.online.base.viewmodel.BaseViewModel
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity1<BaseViewModel, ActivitySplashBinding>(), ImageSliderAdapter.OnLastItemBoundListener {

    private lateinit var viewPager: ViewPager2

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

        val images = listOf(R.mipmap.splogo1, R.mipmap.splogo2, R.mipmap.splogo3)
        val postion = listOf(R.mipmap.postion1, R.mipmap.postion2, R.mipmap.postion3)
        val title = listOf(R.string.title1, R.string.title2, R.string.title3)
        val adapter = ImageSliderAdapter(images,postion,title,this)
        viewPager.adapter = adapter

    }
    override fun onLastItemBound() {
        val currentItem = viewPager.currentItem
        if (currentItem==2){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            viewPager.setCurrentItem((currentItem + 1) , true)
        }
    }
}