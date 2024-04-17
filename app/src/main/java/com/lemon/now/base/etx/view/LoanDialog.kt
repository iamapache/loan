package com.lemon.now.base.etx.view

/**
 *   Lemon Cash
 *  CustomDialog.java
 *
 */
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lemon.now.online.R

class LoanDialog(context: Context) : AlertDialog(context) {
    var dialogTitle: TextView? = null
    var loannotext: TextView? = null
      var logoimg: ImageView? = null
    private  var confirmButton: TextView? = null
    private  var cancelButton: TextView? = null
    private  var contentText: TextView? = null
    private var confirmCallback: (() -> Unit)? = null
    private var cancelCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialogloan)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogTitle = findViewById<TextView>(R.id.dialogTitle)
        contentText = findViewById(R.id.content)

        loannotext = findViewById(R.id.loanno)
        logoimg = findViewById(R.id.logo)

        confirmButton = findViewById(R.id.confirmButton)
        cancelButton = findViewById(R.id.cancelButton)
        contentText?.text="ddddddddd"
        confirmButton?.setOnClickListener {
            confirmCallback?.invoke()
            dismiss()
        }

        cancelButton?.setOnClickListener {
            cancelCallback?.invoke()
            dismiss()
        }
    }

    fun setTitle(title: String) {
        dialogTitle?.text = title
    }

    fun setloannotext(text: String) {
        loannotext?.text = text
    }

    fun setimg(img: String) {
        Glide.with(context).load(img).into(logoimg!!)
    }
    fun setcontent(content: String) {
        contentText?.text = content
    }
    fun setConfirmCallback(callback: () -> Unit) {
        confirmCallback = callback
    }

    fun setCancelCallback(callback: () -> Unit) {
        cancelCallback = callback
    }

}
