package com.lemon.now.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityAuthtipBinding
import com.lemon.now.util.SettingUtil

/**
 *   Lemon Cash
 *  AuthenticationActivity.java
 *
 */
class AuthadfrontTipActivity : BaseActivity1<BaseViewModel, ActivityAuthtipBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        val param1 = intent.getIntExtra("param1", 1)
        if (param1 == 1) {
            mViewBind.tiptop.setBackgroundResource(R.mipmap.adsu)
            mViewBind.tip1.setBackgroundResource(R.mipmap.adee3)
            mViewBind.tip2.setBackgroundResource(R.mipmap.adee2)
            mViewBind.tip3.setBackgroundResource(R.mipmap.adee1)
        } else if (param1 == 2) {
            mViewBind.tiptop.setBackgroundResource(R.mipmap.backsu)
            mViewBind.tip1.setBackgroundResource(R.mipmap.backee1)
            mViewBind.tip2.setBackgroundResource(R.mipmap.backee2)
            mViewBind.tip3.setBackgroundResource(R.mipmap.backee3)
        } else if (param1 == 3) {
            mViewBind.tiptop.setBackgroundResource(R.mipmap.pansu)
            mViewBind.tip1.setBackgroundResource(R.mipmap.panee1)
            mViewBind.tip2.setBackgroundResource(R.mipmap.panee2)
            mViewBind.tip3.setBackgroundResource(R.mipmap.panee3)
        }
        var requestDataLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data?.getStringExtra("url")
                    val intent = intent
                    intent.putExtra("url", data)
                    intent.putExtra("param1", param1)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        mViewBind.next.setOnClickListener {
            val permissions = arrayOf<String?>(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_STATE
            )
            val granted = SettingUtil.arePermissionsGranted(this, permissions)
            if (!granted) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } else {
                val intent = Intent(this@AuthadfrontTipActivity, CameraActivity::class.java)
                intent.putExtra("param1", param1)
                requestDataLauncher.launch(intent)
            }
        }

    }

}

