package com.lemon.now.ui.activity

import ToastUtils
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityPrivacyBinding
import com.lemon.now.ui.model.HomeViewModel

/**
 *   Lemon Cash
 *  PrivacyActivity.java
 *
 */
class PrivacyActivity : BaseActivity1<HomeViewModel, ActivityPrivacyBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

        val style =
            SpannableStringBuilder("By continuing you agree our Terms & Conditions and Privacy Policy.")
        style.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
            28,
            46,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        style.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@PrivacyActivity, WebActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("param1", "value1")
                startActivity(intent)
            }

        }, 28, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        style.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(this@PrivacyActivity, R.color.colorPrimary)
            ),
            51,
            65,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        style.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@PrivacyActivity, WebActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("param1", "value1")
                startActivity(intent)
            }
        }, 51, 65, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mViewBind.agree.movementMethod = LinkMovementMethod.getInstance()
        mViewBind.agree.highlightColor = resources.getColor(android.R.color.transparent)
        mViewBind.agree.text = style
        mViewBind.txrefuse.setOnClickListener {
            finish()
        }
        mViewBind.txagree.setOnClickListener {
            if (!mViewBind.agreeCheckBox.isChecked) {
                ToastUtils.showShort(
                    this@PrivacyActivity,
                    "Please agree with our policy to continue"
                )
            } else {
                if (allPermissionsGranted()) {
                    startActivity(Intent(this@PrivacyActivity, SplashActivity::class.java))
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.READ_SMS,Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE
                        ), SMS_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }

    }

    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 101
        private val REQUEST_WRITE_CONTACTS = 123
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    }

    private fun allPermissionsGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSION_REQUEST_CODE -> {
                if (allPermissionsGranted()) {
                    startActivity(Intent(this@PrivacyActivity, SplashActivity::class.java))
                } else {
                    finish()
                }
            }

            REQUEST_WRITE_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish()
                }
            }
        }
    }
}