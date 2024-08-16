package com.lemon.now.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.ProxyInfo
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Debug
import android.os.Environment
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.facebook.appevents.internal.AppEventUtility.bytesToHex
import com.lemon.now.online.BuildConfig
import com.lemon.now.ui.activity.AuthenticationActivity
import com.lemon.now.ui.activity.OrderActivity
import com.lemon.now.ui.activity.OrderPendingActivity
import com.lemon.now.ui.activity.OrderRepaidActivity
import com.lemon.now.ui.activity.OrderTobeActivity
import com.lemon.now.ui.bean.M7CdaEiz0WPh1Cs3iyzkg6Od
import com.lemon.now.ui.bean.OrderStatusBean
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.Deflater


object SettingUtil {
    fun arePermissionsGranted(activity: Context?, permissions: Array<String?>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    activity!!,
                    permission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun startOtherActivity(context: Context, it: OrderStatusBean, productdd: String) {
        val permissions = arrayOf<String?>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE
        )
        val granted = SettingUtil.arePermissionsGranted(context, permissions)
        if (!granted) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
            return
        }
        when (it.nQvJKMbHsO5F) {
            1 -> {
                val intent = Intent(context, AuthenticationActivity::class.java)
                context.startActivity(intent)
            }

            2 -> {

                val intent = Intent(context, OrderActivity::class.java)
                intent.putExtra("id", productdd)
                intent.putExtra("bean", it.Qbnsde5LgABnpY9IpFTFXkgR3l8)
                context.startActivity(intent)
            }

            3 -> {
                startListActivity(it.M7CdaEiz0WPh1Cs3iyzkg6Od, context)
            }
        }
    }

    fun startListActivity(it: M7CdaEiz0WPh1Cs3iyzkg6Od, context: Context) {
        when (it.S1giqNNoozNFE8yRzARpD2VdSEkMd) {
            1 -> {
                val intent =
                    Intent(context, OrderPendingActivity::class.java)
                intent.putExtra(
                    "titlecontent",
                    "Your application has been received, and we will inform you of the outcome as soon as possible."
                )
                intent.putExtra("title", "Pending")
                intent.putExtra("type", 1)
                intent.putExtra("bean", it)
                context.startActivity(intent)
            }

            2 -> {
                val intent =
                    Intent(context, OrderPendingActivity::class.java)
                intent.putExtra(
                    "titlecontent",
                    "Your application is currently being disbursed. We will notify you once the disbursement is complete."
                )
                intent.putExtra("title", "Disbursing")
                intent.putExtra("type", 2)
                intent.putExtra("bean", it)
                context.startActivity(intent)
            }

            3 -> {
                val intent =
                    Intent(context, OrderTobeActivity::class.java)
                intent.putExtra("title", "To be Repaid")
                intent.putExtra("type", 3)
                intent.putExtra("bean", it)
                context.startActivity(intent)
            }

            4 -> {
                val intent =
                    Intent(context, OrderRepaidActivity::class.java)
                intent.putExtra("title", "Repaid")
                intent.putExtra("bean", it)
                context.startActivity(intent)
            }

            5 -> {
                val intent =
                    Intent(context, OrderPendingActivity::class.java)

                if (it.a2kevgH5EWY9waHNv76F6xKXEwY == 1) {
                    intent.putExtra(
                        "titlecontent",
                        "Please ensure your bank information is accurate and submit application again."
                    )
                    intent.putExtra("title", "Disbursing Fail")
                    intent.putExtra("type", 5)
                } else if (it.a2kevgH5EWY9waHNv76F6xKXEwY == 0) {
                    if (it.EeUgjkb0udXKtKsOyWNChxEzmrn4ZIK46o?.toInt() ?: 0 > 0) {


                        intent.putExtra("type", 55)
                        intent.putExtra("title", "Denied")
                    } else {
                        intent.putExtra(
                            "titlecontent",
                            "Hello, you are now ready to proceed with this application."
                        )
                        intent.putExtra("type", 555)
                        intent.putExtra("title", "Detail")
                    }
                }
                intent.putExtra("bean", it)
                context.startActivity(intent)
            }

            6 -> {
                val intent =
                    Intent(context, OrderTobeActivity::class.java)
                intent.putExtra("title", "Overdue")
                intent.putExtra("type", 6)
                intent.putExtra("bean", it)
                context.startActivity(intent)
            }
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

    fun clearAllCache(activity: AppCompatActivity?) {
        activity?.let {
            deleteDir(it.cacheDir)
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                if (it.externalCacheDir == null) {
                }
                return
            }
            it.externalCacheDir?.let { file ->
                if (deleteDir(file)) {
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun isEmulator(): String {
        var boo = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86")
        if (boo) {
            return "1"
        } else {
            return "0"
        }
        return "0"
    }

    fun isEmulator2(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86")
    }

    fun isDeviceRooted2(): Boolean {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        var file: File? = null

        val paths = arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/system/sbin/",
            "/sbin/",
            "/vendor/bin/",
            "/su/bin/"
        )
        try {
            for (path in paths) {
                file = File(path + "su")
                if (file.exists() && file.canExecute()) {
                    return true
                }
            }
        } catch (x: java.lang.Exception) {
            x.printStackTrace()
        }
        var file2: File? = null
        try {
            for (path in paths) {
                file2 = File(path + "busybox");
                if (file2.exists() && file2.canExecute()) {
                    return true
                }
            }
        } catch (x: java.lang.Exception) {
            x.printStackTrace()
        }
        return false
    }

    fun String.base64(): String {
        try {
            val comp = Deflater(Deflater.BEST_COMPRESSION)
            comp.setInput(this.toByteArray())
            comp.finish()

            ByteArrayOutputStream(256).use { outputStream ->
                while (!comp.finished()) {
                    val bytes = ByteArray(256)
                    val length = comp.deflate(bytes)
                    outputStream.write(bytes, 0, length)
                }
                return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            return ""
        }
    }

    fun isDeviceRooted(): Int {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return 1
        }

        var file: File? = null

        val paths = arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/system/sbin/",
            "/sbin/",
            "/vendor/bin/",
            "/su/bin/"
        )
        try {
            for (path in paths) {
                file = File(path + "su")
                if (file.exists() && file.canExecute()) {
                    return 1
                }
            }
        } catch (x: java.lang.Exception) {
            x.printStackTrace()
        }
        var file2: File? = null
        try {
            for (path in paths) {
                file2 = File(path + "busybox");
                if (file2.exists() && file2.canExecute()) {
                    return 1
                }
            }
        } catch (x: java.lang.Exception) {
            x.printStackTrace()
        }
        return 0
    }

    private const val PERMISSION_READ_PHONE_STATE = 101

    @SuppressLint("SuspiciousIndentation")
    fun getAvailableSimSlots(context: Context): Int {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var availableSimSlots = 0
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_PHONE_STATE
            )
        } else {
            if (telephonyManager.phoneCount > 1) {
                availableSimSlots = telephonyManager.phoneCount
            }
        }

        return availableSimSlots
    }

    @SuppressLint("SuspiciousIndentation")
    fun getActivatedSimCount(context: Context): Int {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_PHONE_STATE
            )
            return 0
        } else {
            var simState: Int = 0
            var numSimCards = 0
            for (i in 1 until getAvailableSimSlots(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    simState = telephonyManager.getSimState(i)
                }
                if (simState == TelephonyManager.SIM_STATE_READY) {
                    numSimCards++
                }
            }

            return numSimCards
        }

    }

    fun isVpnConnected(context: Context): Int {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networks = connectivityManager.allNetworks
            for (network in networks) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (networkCapabilities != null && networkCapabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_VPN
                    )
                ) {
                    return 1
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_VPN) {
                return 1
            }
        }
        return 0
    }

    fun isUsingProxy(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val network = connectivityManager.activeNetwork
            val proxyInfo = connectivityManager.getLinkProperties(network)
            return proxyInfo?.isPrivateDnsActive() != null
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        var isNetworkAvailable: Boolean = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isNetworkAvailable = true
                }

                override fun onLost(network: Network) {
                    isNetworkAvailable = false
                }
            })
        return isNetworkAvailable
    }

    fun isDNSAvailable(): Boolean {
        val address = InetAddress.getByName("www.google.com")
        return address != null && address.hostAddress.isNotEmpty()
    }

    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress) {
                        val ip = address.hostAddress
                        val isIPv4 = ip.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return ip
                        } else {
                            if (!isIPv4) {
                                val delim = ip.indexOf('%')
                                return if (delim < 0) ip.toUpperCase() else ip.substring(0, delim)
                                    .toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    var sleepTime = SystemClock.uptimeMillis()

    fun calculateSleepDuration(): Long {
        val wakeUpTime = SystemClock.uptimeMillis()
        val sleepDuration = wakeUpTime - sleepTime
        return sleepDuration
    }

    fun getLastUpdateTime(packageManager: PackageManager, packageName: String): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val lastUpdateTime = packageInfo.lastUpdateTime
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            sdf.format(Date(lastUpdateTime))
        } catch (e: PackageManager.NameNotFoundException) {
            "no"
        }
    }

    fun firstInstallTime(packageManager: PackageManager, packageName: String): String {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return packageInfo.firstInstallTime.toString()
    }


    fun getCountryCode(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso
    }

    fun getCountryCode2(context: Context): String {
        val locale = context.resources.configuration.locale
        return locale.country
    }

    fun getDisplayLanguage(context: Context): String {
        val locale = context.resources.configuration.locale
        return locale.displayLanguage
    }

    fun getSystemLanguage(): String {
        val systemLocale = Locale.getDefault()
        val systemLanguageCode = systemLocale.language
        return systemLanguageCode
    }

    fun isO3Language(): String {
        val systemLocale = Locale.getDefault()
        val systemLanguageCode = systemLocale.isO3Language
        return systemLanguageCode
    }

    fun getProcessCpuUsage(): Int {
        val processCpuInfo = Debug.getBinderProxyObjectCount()
        val systemCpuInfo = Debug.threadCpuTimeNanos()

        val cpuUsage = if (systemCpuInfo > 0) {
            (processCpuInfo.toDouble() / systemCpuInfo.toDouble()) * 100
        } else {
            0.0
        }
        return cpuUsage.toInt()
    }

    fun getMobileType(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkType = when (telephonyManager.phoneType) {
            TelephonyManager.PHONE_TYPE_GSM -> "1"
            TelephonyManager.PHONE_TYPE_CDMA -> "2"
            TelephonyManager.PHONE_TYPE_SIP -> "3"
            else -> "1"
        }
        return networkType
    }

    fun isLowPowerModeEnabled(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            powerManager.isPowerSaveMode
        } else {
            false
        }
    }

    fun isAutoBrightnessEnabled(context: Context): Boolean {
        val contentResolver: ContentResolver = context.contentResolver
        return try {
            val mode =
                Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
            mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    fun getNetworkOperatorName(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkOperatorName
    }

    fun isDebugMode(): Boolean {
        return BuildConfig.DEBUG
    }

    fun isSimCardReady(context: Context): Boolean {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telephonyManager.simState
        return simState == TelephonyManager.SIM_STATE_READY
    }

    fun getNetworkOperatorName2(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkOperator ?: "unknown"
    }

    fun getNetworkCountryIso(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso ?: "unknown"
    }

    fun getNetworkOperatorName3(context: Context): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkOperatorName
    }

    fun getApplicationSignatureMD5(context: Context): String {
        try {
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
            val md = MessageDigest.getInstance("MD5")
            for (signature in signatures) {
                md.update(signature.toByteArray())
            }
            val signatureMD5Bytes = md.digest()
            val signatureMD5StringBuilder = StringBuilder()
            for (byte in signatureMD5Bytes) {
                signatureMD5StringBuilder.append(String.format("%02x", byte))
            }
            return signatureMD5StringBuilder.toString()
        } catch (e: Exception) {
        }
        return ""
    }

    fun getApplicationSignatureSHA1(context: Context): String {
        val packageName = context.packageName
        val packageManager = context.packageManager
        val signatures =
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        val signature = signatures[0]
        val messageDigest = MessageDigest.getInstance("SHA1")
        messageDigest.update(signature.toByteArray())
        val signatureBytes = messageDigest.digest()
        val stringBuilder = StringBuilder()
        for (signatureByte in signatureBytes) {
            stringBuilder.append(String.format("%02X", signatureByte))
        }
        return stringBuilder.toString()
    }

    fun getWifiSSID(context: Context): String {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        return wifiInfo?.ssid ?: ""
    }

    fun getSHA256(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            val signatures: Array<Signature> = packageInfo.signatures
            val md = MessageDigest.getInstance("SHA-256")
            md.update(signatures[0].toByteArray())
            val digest = md.digest()
            bytesToHex(digest)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    private fun bytesToHex(bytes: ByteArray): String? {
        val builder = java.lang.StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }

    //net
    open fun getNetworkType(context: Context): String? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) {
            val type = activeNetwork.type
            when (type) {
                ConnectivityManager.TYPE_WIFI -> "wifi"
                ConnectivityManager.TYPE_MOBILE -> {
                    val subType = activeNetwork.subtype
                    when (subType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2g"

                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3g"

                        TelephonyManager.NETWORK_TYPE_LTE -> "4g"
                        TelephonyManager.NETWORK_TYPE_NR -> "5g"
                        else -> "mobile"
                    }
                }

                else -> "other"
            }
        } else {
            "other"
        }
    }

    fun is4GNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.subtype == TelephonyManager.NETWORK_TYPE_LTE
            }
        }
        return false
    }

    fun is5GNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.subtype == TelephonyManager.NETWORK_TYPE_NR
            }
        }
        return false
    }

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    fun isMobileDataEnabled(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
                && telephonyManager.dataState == TelephonyManager.DATA_CONNECTED
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getIPAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getIPv4Address(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (address is Inet4Address && !address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getWifiIPAddress(context: Context): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        val ipAddress: Int? = wifiInfo?.ipAddress
        return if (ipAddress != null) {
            val ipString = String.format(
                "%d.%d.%d.%d",
                ipAddress and 0xff,
                ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )
            ipString
        } else {
            null
        }
    }

    fun getWifiSubnetMask(context: Context): String? {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wifiManager.dhcpInfo.netmask)
    }

    fun getWifiDns1(context: Context): String? {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wifiManager.dhcpInfo.dns1)
    }

    fun getCurrentWifiSSID(context: Context): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        return wifiInfo?.ssid
    }

    fun getCurrentWifiMacAddress(context: Context): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        return wifiInfo?.macAddress
    }

    fun getWifiGatewayIPAddress(context: Context): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcpInfo = wifiManager.dhcpInfo

        val gatewayIpAddress = dhcpInfo.gateway

        return if (gatewayIpAddress != 0) {
            val gatewayInetAddress = InetAddress.getByAddress(intToByteArray(gatewayIpAddress))
            gatewayInetAddress.hostAddress
        } else {
            null
        }
    }

    private fun intToByteArray(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            (value shr 8 and 0xFF).toByte(),
            (value shr 16 and 0xFF).toByte(),
            (value shr 24 and 0xFF).toByte()
        )
    }

    fun isAPPPhone(context: Context): Boolean {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    fun isAPPTablet(context: Context): Boolean {
        val configuration: Configuration = context.resources.configuration
        return (configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    fun getNetworkWifiLevel(context: Context): Int {
        if (!isWifiConnected(context)) {
            return 0
        }
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val level = wifiInfo.rssi
            return if (level <= 0 && level >= -50) {
                1
            } else if (level < -50 && level >= -70) {
                2
            } else if (level < -70 && level >= -80) {
                3
            } else if (level < -80 && level >= -100) {
                4
            } else {
                5
            }
        }
        return 0
    }

    fun getProxyTypeForWiFi(context: Context): String? {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val linkProperties = connectivityManager.getLinkProperties(network)

        if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            linkProperties?.let {
                val proxyInfo: ProxyInfo? = it.httpProxy
                if (proxyInfo != null) {
                    val proxyHost = proxyInfo.host
                    val proxyPort = proxyInfo.port
                    val proxyType = when {
                        proxyInfo?.exclusionList?.isNotEmpty() == true -> return "PAC"
                        proxyHost?.isNotEmpty() == true -> return "Manual"
                        else -> return "None"
                    }
                } else {
                }
            } ?: run {
            }
        } else {
        }
        return "NONE"
    }

    fun isScreenLocked(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardLocked
    }

    fun getBatteryStatus(context: Context): String {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryStatus = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        when (batteryStatus) {
            BatteryManager.BATTERY_STATUS_UNKNOWN -> {
                return "1"
            }

            BatteryManager.BATTERY_STATUS_CHARGING -> {
                return "2"
            }

            BatteryManager.BATTERY_STATUS_DISCHARGING -> {
                return "3"
            }

            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> {
                return "4"
            }

            BatteryManager.BATTERY_STATUS_FULL -> {
                return "5"
            }
        }
        return "1"
    }

    fun getTotalBatteryCapacity(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val totalCapacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        return totalCapacity
    }

    fun getBatteryPercentage(context: Context): Int {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return (level * 100 / scale.toFloat()).toInt()
    }

    fun isCharging(context: Context): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    fun isACCharging(context: Context): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        return chargePlug == BatteryManager.BATTERY_PLUGGED_AC
    }

    fun isUSBCharging(context: Context): Boolean {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        return chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    }
}
