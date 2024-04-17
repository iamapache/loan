package com.lemon.now.ui.model

class MessageEvent internal constructor(status: Int,message: String) {
    companion object {
        const val login: Int = 1
        const val au: Int = 2
    }
    private var message: String? = null
    private var status: Int? = 0
    init {
        this.message = message
        this.status = status
    }
    internal fun getMessage(): String? {
        return message
    }
    fun setMessage(message: String) {
        this.message = message
    }

    internal fun getStatus(): Int? {
        return status
    }
    fun seStatus(status: Int) {
        this.status = status
    }

}
