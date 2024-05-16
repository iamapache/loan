package com.lemon.now.base.etx.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


private var loadingDialog: CustomLoadingDialog? = null
private var loadingDialog2: CustomLoadingDialog? = null
fun AppCompatActivity.showLoadingExt(message: String = "loading") {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = CustomLoadingDialog(this,message)
        }
        loadingDialog?.show()
    }
}
fun AppCompatActivity.showLoadingExt2(message: String = "loading") {
    if (!this.isFinishing) {
        if (loadingDialog2 == null) {
            loadingDialog2 = CustomLoadingDialog(this,message)
            loadingDialog2?.setCancelable(false);
        }
        loadingDialog2?.show()
    }
}

fun Activity.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}
fun Activity.dismissLoadingExt2() {
    loadingDialog2?.dismiss()
    loadingDialog2 = null
}
fun Fragment.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}
