package com.lemon.now.base.etx.view

/**
 *   Lemon Cash
 *  CustomDialog.java
 *
 */
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.lemon.now.online.R

class CustomDialogFail(context: Context) : AlertDialog(context) {

    private  var confirmButton: TextView? = null
    private  var cancelButton: TextView? = null
    private  var contentText: TextView? = null
    private var confirmCallback: (() -> Unit)? = null
    private var cancelCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialogfail)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        contentText = findViewById(R.id.content)
        confirmButton = findViewById(R.id.confirmButton)
        cancelButton = findViewById(R.id.cancelButton)
        confirmButton?.setOnClickListener {
            confirmCallback?.invoke()
            dismiss()
        }

        cancelButton?.setOnClickListener {
            cancelCallback?.invoke()
            dismiss()
        }
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
