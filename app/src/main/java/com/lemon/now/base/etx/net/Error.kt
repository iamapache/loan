package com.lemon.now.base.etx.net

enum class Error(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, "Network Error"),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "Network Error"),
    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, "Network Error"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "Network Error"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "timeout");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}