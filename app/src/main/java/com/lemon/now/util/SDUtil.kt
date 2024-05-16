package com.lemon.now.util

import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import java.io.File


/**
 *   Lemon Cash
 *  SDUtil.java
 *
 */
object SDUtil {

    fun getRootDirectorytotalSpace(): Long {
        val totalInternalMemorySize = File(Environment.getRootDirectory().absolutePath).totalSpace?: 0L
        val totalExternalMemorySize = Environment.getExternalStorageDirectory().totalSpace?: 0L
        return totalInternalMemorySize+totalExternalMemorySize ?: 0L
    }

    fun availableInternalMemorySize(): Long {
        val availableInternalMemorySize =
            File(Environment.getRootDirectory().absolutePath).freeSpace?: 0L
        val availableExternalMemorySize = Environment.getExternalStorageDirectory().freeSpace?: 0L
        return availableInternalMemorySize+availableExternalMemorySize ?: 0L
    }
    fun getUsedStoragePercentage(): Double {
        return (getRootDirectorytotalSpace() / availableInternalMemorySize()) * 100.0
    }

    fun getExternalStorageSize(): Long {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        return externalStorageDirectory?.totalSpace ?: 0L
    }

    fun getExternalfreeSpaceStorageSize(): Long {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        return externalStorageDirectory?.freeSpace ?: 0L
    }


    fun getTotalMemory(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager!!.getMemoryInfo(memoryInfo)

        val totalMemory = memoryInfo.totalMem
        return totalMemory ?: 0L
    }

    fun getavailableMemory(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager!!.getMemoryInfo(memoryInfo)

        val availableMemory = memoryInfo.availMem
        return availableMemory ?: 0L
    }

    fun isSDCardAvailable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}