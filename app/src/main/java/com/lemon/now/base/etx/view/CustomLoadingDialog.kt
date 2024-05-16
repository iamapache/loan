package com.lemon.now.base.etx.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.lemon.now.base.etx.util.getDisplayApiL
import com.lemon.now.base.etx.util.getDisplayApiR
import com.lemon.now.online.R


/**
 *   Lemon Cash
 *  CustomLoadingDialog.java
 *
 */
class CustomLoadingDialog(context: Context?,textx: String?) : AlertDialog(
    context!!
) {
    private var text:String =""
    init {
        text = textx.toString()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        getWindow()?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow()?.setGravity(Gravity.CENTER);
        setContentView(R.layout.layout_custom_progress_dialog_view)
        setCanceledOnTouchOutside(false)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val textView = findViewById<TextView>(R.id.loading_tips)
        val dialogWindow: Window = window!!
        val m: Display? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getDisplayApiR(context)
        } else {
            getDisplayApiL(context)
        }

        val p = dialogWindow.attributes
        p.width = (m?.width?:0 * 0.95).toInt()
        p.gravity = Gravity.CENTER
        dialogWindow.attributes = p
        textView?.text =text
    }
    fun showLoading() {
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.CENTER
        window!!.attributes = layoutParams
        show()
    }

    fun hideLoading() {
        hide()
    }
}