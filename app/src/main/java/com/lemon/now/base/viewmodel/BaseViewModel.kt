package com.lemon.now.base.viewmodel

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { LoadingLiveData<String>() }
        val dismissDialog by lazy { LoadingLiveData<Boolean>() }
    }

}