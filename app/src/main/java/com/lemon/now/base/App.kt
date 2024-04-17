package com.lemon.now.base

import SPUtil
import android.app.Activity
import android.os.Bundle
import android.support.multidex.MultiDex
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.lemon.now.base.etx.BaseApp
import com.lemon.now.ui.ApiService


class App : BaseApp() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SPUtil.setContext(this)

        val appToken: String = "315msipsqygw"
        val environment: String = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(this, appToken, environment)
        config.setUrlStrategy(AdjustConfig.URL_STRATEGY_CHINA);
        config.setOnEventTrackingSucceededListener { eventSuccessResponseData ->
            Log.v(
                "println",
                "requestCode：" + eventSuccessResponseData.eventToken + eventSuccessResponseData.message
            )
        }
        config.setOnAttributionChangedListener {
            val network: String = it.network
            var channel:   String by SPUtil(ApiService.channel, "Other")
            channel=network
        }
        var firsttime:   String by SPUtil(ApiService.firsttime, "")
        firsttime=System.currentTimeMillis().toString()
        Adjust.onCreate(config)
        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())
        MultiDex.install(this)
    }
    private class AdjustLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            Adjust.onPause()
        }

        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {} //...
    }
}
