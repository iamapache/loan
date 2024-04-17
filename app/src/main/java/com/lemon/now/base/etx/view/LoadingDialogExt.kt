package com.lemon.now.base.etx.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


private var loadingDialog: CustomLoadingDialog? = null

fun AppCompatActivity.showLoadingExt(message: String = "loading") {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = CustomLoadingDialog(this,message)
        }
        loadingDialog?.show()
    }
}

fun Activity.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

fun Fragment.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}
