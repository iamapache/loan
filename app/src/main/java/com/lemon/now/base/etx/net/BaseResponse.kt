package com.lemon.now.base.etx.net

abstract class BaseResponse<T> {

    abstract fun isSucces(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}