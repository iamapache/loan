package com.lemon.now.base.etx.net


data class ApiResponse<T>(val rZ81DSU7WU4hny4ukGHljvjO41bfB: Int, val vWCgp64OkxPVoGqics: String, val data: T) : BaseResponse<T>() {

    override fun isSucces() = rZ81DSU7WU4hny4ukGHljvjO41bfB == 1

    override fun getResponseCode() = rZ81DSU7WU4hny4ukGHljvjO41bfB

    override fun getResponseData() = data

    override fun getResponseMsg() = vWCgp64OkxPVoGqics

}