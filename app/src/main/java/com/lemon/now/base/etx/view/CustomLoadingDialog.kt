package com.lemon.now.base.etx.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
import android.widget.TextView
import com.lemon.now.online.R

/**
 *   Lemon Cash
 *  CustomLoadingDialog.java
 *
 */
class CustomLoadingDialog(context: Context?,text: String?) : Dialog(
    context!!
) {
    init {
        setContentView(R.layout.layout_custom_progress_dialog_view)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val textView = findViewById<TextView>(R.id.loading_tips)

        textView.text =text
    }

    fun showLoading() {
        show()
    }

    fun hideLoading() {
        hide()
    }
}