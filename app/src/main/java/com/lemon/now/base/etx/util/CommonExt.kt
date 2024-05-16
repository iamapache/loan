package com.lemon.now.base.etx.util

import android.content.Context
import android.os.Build
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Checkable
import androidx.annotation.RequiresApi


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

/**
 * 兼容API获取屏幕信息
 * @param context
 * @return
 */
fun getDisplay(context: Context): Display? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getDisplayApiR(context)
    } else {
        getDisplayApiL(context)
    }
}

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
 fun getDisplayApiL(context: Context): Display? {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay
}

@RequiresApi(api = Build.VERSION_CODES.R)
 fun getDisplayApiR(context: Context): Display? {
    return context.display
}

inline fun <T : View> T.singleClick(time: Long = 1000, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 800) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0
