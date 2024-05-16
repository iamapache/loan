package com.lemon.now.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

/**
 *   Lemon Cash
 *  BatteryReceiver.java
 *
 */
class BatteryReceiver : BroadcastReceiver() {

    private var batteryLevel = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        }
    }

    fun getBatteryLevel(): Int {
        return batteryLevel
    }
}