package com.lemon.now.base.etx.util

import android.view.View

inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}
fun View.gone() {
    visibility = View.GONE
}


