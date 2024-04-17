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

class CustomDialog2(context: Context) : AlertDialog(context) {

    private  var confirmButton: TextView? = null
    private  var contentText: TextView? = null
    private var confirmCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog2)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        contentText = findViewById(R.id.content)
        confirmButton = findViewById(R.id.confirmButton)
        confirmButton?.setOnClickListener {
            confirmCallback?.invoke()
            dismiss()
        }

    }

    fun setConfirmCallback(callback: () -> Unit) {
        confirmCallback = callback
    }


}
