package com.bjxapp.online.base

import SPUtil
import android.support.multidex.MultiDex
import com.bjxapp.online.base.etx.BaseApp


class App : BaseApp() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SPUtil.setContext(this)
        MultiDex.install(this)
    }

}
