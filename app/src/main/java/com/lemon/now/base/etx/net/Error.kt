package com.lemon.now.base.etx.net

enum class Error(private val code: Int, private val err: String) {

    UNKNOWN(1000, "Network Error"),
    PARSE_ERROR(1001, "Network Error"),
    NETWORK_ERROR(1002, "Network Error"),
    SSL_ERROR(1004, "Network Error"),
    TIMEOUT_ERROR(1006, "timeout");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}