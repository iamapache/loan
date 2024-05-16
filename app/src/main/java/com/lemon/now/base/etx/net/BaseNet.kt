package com.lemon.now.base.etx.net

import androidx.lifecycle.viewModelScope
import com.lemon.now.base.etx.util.loge
import com.lemon.now.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch





fun <T> BaseViewModel.getData(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "loading..."
): Job {
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    return viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
            loadingChange.dismissDialog.postValue(false)
            success(it)
        }.onFailure {
            loadingChange.dismissDialog.postValue(false)
            it.message?.loge()
            it.printStackTrace()
            error(ExceptionHandle.handleException(it))
        }
    }
}

fun <T> BaseViewModel.getData2(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "loading..."
): Job {
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    return viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
            success(it)
        }.onFailure {
            loadingChange.dismissDialog.postValue(false)
            it.message?.loge()
            it.printStackTrace()
            error(ExceptionHandle.handleException(it))
        }
    }
}

