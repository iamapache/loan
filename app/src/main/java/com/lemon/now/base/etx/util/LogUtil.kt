package com.lemon.now.base.etx.util

import android.util.Log

const val TAG = "xxx"
var jetpackMvvmLog = true

private enum class LEVEL {
    V, D, I, W, E
}

fun String.logv(tag: String = TAG) =
    log(LEVEL.V, tag, this)
fun String.logd(tag: String = TAG) =
    log(LEVEL.D, tag, this)
fun String.logi(tag: String = TAG) =
    log(LEVEL.I, tag, this)
fun String.logw(tag: String = TAG) =
    log(LEVEL.W, tag, this)
fun String.loge(tag: String = TAG) =
    log(LEVEL.E, tag, this)

private fun log(level: LEVEL, tag: String, message: String) {
    if (!jetpackMvvmLog) return

    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}
private const val LOG_MAXLENGTH = 2000

fun e(TAG: String, msg: String) {
    val strLength = msg.length
    var start = 0
    var end = LOG_MAXLENGTH
    for (i in 0..99) {
        //剩下的文本还是大于规定长度则继续重复截取并输出
        if (strLength > end) {
            Log.e(TAG + i, msg.substring(start, end))
            start = end
            end = end + LOG_MAXLENGTH
        } else {
            Log.e(TAG, msg.substring(start, strLength))
            break
        }
    }
}