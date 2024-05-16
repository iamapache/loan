package com.lemon.now.ui.activity

import ToastUtils
import android.content.Intent
import android.graphics.BitmapFactory

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.util.logv
import com.lemon.now.base.etx.view.dismissLoadingExt
import com.lemon.now.base.etx.view.dismissLoadingExt2
import com.lemon.now.online.databinding.ActivityAuthenticationBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.bean.AWSBean
import com.lemon.now.ui.bean.Userbean
import com.lemon.now.ui.model.AuthModel
import com.lemon.now.ui.model.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


/**
 *   Lemon Cash
 *  AuthenticationActivity.java
 *
 */
class AuthenticationActivity : BaseActivity1<AuthModel, ActivityAuthenticationBinding>() {

    var bitmap1 = ""
    var bitmap2 = ""
    var bitmap3 = ""
    var param1 = 1
    var userbean: Userbean? = null
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }
        mViewModel.authinfo()
        var requestDataLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {


                    param1 = result.data?.getIntExtra("param1", 1)!!
                    if (param1 == 1) {
                        bitmap1 = result.data?.getStringExtra("url").toString()
                        if (bitmap1.isNotEmpty()) {
                            mViewBind.adfrontimg.setImageBitmap(
                                BitmapFactory.decodeFile(
                                    File(
                                        bitmap1
                                    ).absolutePath
                                )
                            )
                        }
                        if (bitmap2.isNotEmpty()) {
                            mViewBind.adbackimg.setImageBitmap(BitmapFactory.decodeFile(File(bitmap2).absolutePath))
                        }
                        if (bitmap3.isNotEmpty()) {
                            mViewBind.adpanimg.setImageBitmap(BitmapFactory.decodeFile(File(bitmap3).absolutePath))
                        }
                    } else if (param1 == 2) {
                        bitmap2 = result.data?.getStringExtra("url").toString()
                        if (bitmap1.isNotEmpty()) {
                            mViewBind.adfrontimg.setImageBitmap(
                                BitmapFactory.decodeFile(
                                    File(
                                        bitmap1
                                    ).absolutePath
                                )
                            )
                        }
                        if (bitmap2.isNotEmpty()) {
                            mViewBind.adbackimg.setImageBitmap(BitmapFactory.decodeFile(File(bitmap2).absolutePath))
                        }
                        if (bitmap3.isNotEmpty()) {
                            mViewBind.adpanimg.setImageBitmap(BitmapFactory.decodeFile(File(bitmap3).absolutePath))
                        }
                    } else if (param1 == 3) {
                        bitmap3 = result.data?.getStringExtra("url").toString()
                        if (bitmap1.isNotEmpty()) {
                            mViewBind.adfrontimg.setImageBitmap(
                                BitmapFactory.decodeFile(
                                    File(
                                        bitmap1
                                    ).absolutePath
                                )
                            )
                        }
                        if (bitmap2.isNotEmpty()) {
                            mViewBind.adbackimg.setImageBitmap(BitmapFactory.decodeFile(File(bitmap2).absolutePath))
                        }
                        if (bitmap3.isNotEmpty()) {
                            mViewBind.adpanimg.setImageBitmap(BitmapFactory.decodeFile(File(bitmap3).absolutePath))
                        }
                    }
                    bitmap1.toString().logv()
                    mViewModel.aws()
                }
            }
        mViewBind.adfront.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, AuthadfrontTipActivity::class.java)
            intent.putExtra("param1", 1)
            requestDataLauncher.launch(intent)
        }
        mViewBind.adback.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, AuthadfrontTipActivity::class.java)
            intent.putExtra("param1", 2)
            requestDataLauncher.launch(intent)
        }
        mViewBind.adpan.setOnClickListener {
            val intent = Intent(this@AuthenticationActivity, AuthadfrontTipActivity::class.java)
            intent.putExtra("param1", 3)
            requestDataLauncher.launch(intent)
        }
        userbean = Userbean()

        mViewBind.next.setOnClickListener {
            val adjustEvent = AdjustEvent(ApiService.step1)
            Adjust.trackEvent(adjustEvent)
            if (userbean?.userNames?.isNotEmpty() == true && userbean?.userNumber?.isNotEmpty() == true && userbean?.date?.isNotEmpty() == true
                && userbean?.panNumber?.isNotEmpty() == true && userbean?.address?.isNotEmpty() == true
                && userbean?.frontimg?.isNotEmpty() == true && userbean?.backimg?.isNotEmpty() == true
                && userbean?.male?.isNotEmpty() == true
            ) {
                val map = hashMapOf(
                    "ZbspMUi4YGmGq0ioNH6mrzQcHCsGrz883Ed" to "1",
                    "JagPyb82zgGT3vxOUXzqsvYoli410PMB" to userbean?.frontimg.toString(),
                    "aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9" to userbean?.backimg.toString(),
                    "RVdeam" to userbean?.userNames.toString(),
                    "QEwbbq7EWftpSMsC8ACVRbpKmLb" to userbean?.userNumber.toString(),
                    "j3go0Or3oSXdLKCVoXSWqH4YFDlGxfeuF5SR" to userbean?.date.toString(),
                    "ByBVgtNcR1nQgAIykVh9srO0b2XMFzj" to userbean?.male.toString(),
                    "parSQe8ddYXQh" to userbean?.cardImg.toString(),
                    "UeFqjoQ5IDuu8B3Zabq0" to userbean?.panNumber.toString(),
                    "libETpAU" to userbean?.address.toString()

                )
                mViewModel.submitAuth(map)

            } else {
                ToastUtils.showShort(this@AuthenticationActivity, "Please upload Pan card photo")
            }

        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {
        if (messageEvent.getStatus() == MessageEvent.finish) {
            finish()
        }
    }

    private fun updateimg(awsbean: AWSBean, param1: Int, file: File) {

        val credentials = BasicSessionCredentials(
            awsbean?.ckERDTGankxlbEoezBJ47wlSl?.accessKeyId,
            awsbean?.ckERDTGankxlbEoezBJ47wlSl?.secretAccessKey,
            awsbean?.ckERDTGankxlbEoezBJ47wlSl?.sessionToken
        )
        val s3Client =
            AmazonS3Client(credentials, Region.getRegion(awsbean?.Jkm3b3aNx3Ef8B98NX))
        val bucketName = awsbean?.dxpiLILEoy0bCkx
        val key = "${awsbean?.q8vwEJMot33YpsV2okuZe67ym1Eh}${
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(System.currentTimeMillis())
        }/${file.name}"
        val transferUtility =
            TransferUtility.builder().context(applicationContext)
                .s3Client(s3Client).build()
        val observer: TransferObserver = transferUtility.upload(bucketName, key, file)
        observer.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state.toString() === "COMPLETED") {
                    val imageUrl = "${awsbean?.ql04hxK6f7XCrL47zFHGrHl7STBwm98}/$key"
                    Log.i("S3", "上传完成" + imageUrl)
                    dismissLoadingExt()
                    if (param1 == 1) {
                        val message = Message()
                        message.what = WORK_COMPLETED
                        message.obj = key
                        handler.sendMessage(message)
                    } else if (param1 == 2) {
                        val message = Message()
                        message.what = WORK_COMPLETED2
                        message.obj = key
                        handler.sendMessage(message)
                    } else if (param1 == 3) {
                        val message = Message()
                        message.what = WORK_COMPLETED3
                        message.obj = key
                        handler.sendMessage(message)
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.i("S3", "上传进度：$bytesCurrent/$bytesTotal")
            }

            override fun onError(id: Int, e: Exception) {
                dismissLoadingExt2()
            }
        })
    }

    private val WORK_COMPLETED = 1
    private val WORK_COMPLETED2 = 2
    private val WORK_COMPLETED3 = 3
    private var falg = 0
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == WORK_COMPLETED) {
                falg = 1
                val key = msg.obj as String
                mViewModel.ocr(key, "AADHAAR_FRONT")
            } else if (msg.what == WORK_COMPLETED2) {
                falg = 2
                val key = msg.obj as String
                mViewModel.ocr(key, "AADHAAR_BACK")
            } else if (msg.what == WORK_COMPLETED3) {
                falg = 3
                val key = msg.obj as String
                mViewModel.ocr(key, "PAN_FRONT")
            }
        }
    }


    override fun createObserver() {

        mViewModel.commonData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (param1 == 1) {
                    updateimg(it, param1, File(bitmap1))
                } else if (param1 == 2) {
                    updateimg(it, param1, File(bitmap2))
                } else if (param1 == 3) {
                    updateimg(it, param1, File(bitmap3))
                }
            } else {
            }
        })
        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val intent = Intent(this@AuthenticationActivity, AuthInfoActivity::class.java)
                intent.putExtra("contactNum", 5)
                intent.putExtra("userbean", userbean)
                startActivity(intent)
//                val intent = intent
//                intent.putExtra("userbean", userbean)
//                intent.putExtra("contactNum",5)
//                intent.setClass(this@AuthenticationActivity, BankInfoActivity::class.java)
//                startActivity(intent)
            } else {
                ToastUtils.showShort(this@AuthenticationActivity, it.vWCgp64OkxPVoGqics)
            }
        })

        mViewModel.authinfoData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (it.JagPyb82zgGT3vxOUXzqsvYoli410PMB.isNotEmpty() && it.RVdeam.isNotEmpty() && it.QEwbbq7EWftpSMsC8ACVRbpKmLb.isNotEmpty()
                    && it.ByBVgtNcR1nQgAIykVh9srO0b2XMFzj.isNotEmpty() && it.j3go0Or3oSXdLKCVoXSWqH4YFDlGxfeuF5SR.isNotEmpty()
                    && it.JagPyb82zgGT3vxOUXzqsvYoli410PMB.isNotEmpty() && it.libETpAU.isNotEmpty()
                    && it.aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9.isNotEmpty() && it.UeFqjoQ5IDuu8B3Zabq0.isNotEmpty()
                    && it.parSQe8ddYXQh.isNotEmpty()&& it.NfdUWBoWLpCLqyf83El.isNotEmpty()&& it.DKrmZAi6bHIGWRJN4qbI3yF5dnLuuo12hBh.isNotEmpty()&& it.QI7zJuHhm40svgmoacMA41EyG7Onn.isNotEmpty()
                ) {
                    userbean?.userNames = it.RVdeam.toString()
                    userbean?.userNumber = it.QEwbbq7EWftpSMsC8ACVRbpKmLb.toString()
                    userbean?.male = it.ByBVgtNcR1nQgAIykVh9srO0b2XMFzj.toString()
                    userbean?.date = it.j3go0Or3oSXdLKCVoXSWqH4YFDlGxfeuF5SR.toString()
                    userbean?.frontimg = it.JagPyb82zgGT3vxOUXzqsvYoli410PMB.toString()

                    userbean?.address = it.libETpAU.toString()
                    userbean?.backimg = it.aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9.toString()

                    userbean?.panNumber = it.UeFqjoQ5IDuu8B3Zabq0.toString()
                    userbean?.cardImg = it.parSQe8ddYXQh.toString()
                    if (it.JagPyb82zgGT3vxOUXzqsvYoli410PMB.isNotEmpty()) {
                        Glide.with(this)
                            .load(it.Cjzv3rIl2K6E3K + "/" + it.JagPyb82zgGT3vxOUXzqsvYoli410PMB)
                            .into(mViewBind.adfrontimg)
                    }
                    if (it.aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9.isNotEmpty()) {
                        Glide.with(this)
                            .load(it.Cjzv3rIl2K6E3K + "/" + it.aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9)
                            .into(mViewBind.adbackimg)
                    }
                    if (it.parSQe8ddYXQh.isNotEmpty()) {
                        Glide.with(this).load(it.Cjzv3rIl2K6E3K + "/" + it.parSQe8ddYXQh)
                            .into(mViewBind.adpanimg)
                    }
                }
            }
        })
        mViewModel.orcData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (falg == 1) {
                    userbean?.userNames = it.RVdeam.toString()
                    userbean?.userNumber = it.QEwbbq7EWftpSMsC8ACVRbpKmLb.toString()
                    userbean?.male = it.ByBVgtNcR1nQgAIykVh9srO0b2XMFzj.toString()
                    userbean?.date = it.j3go0Or3oSXdLKCVoXSWqH4YFDlGxfeuF5SR.toString()
                    userbean?.frontimg = it.BLmHMFEzm0jYVS6vWTeAg8Bo9x.toString()
                } else if (falg == 2) {
                    userbean?.address = it.libETpAU.toString()
                    userbean?.backimg = it.BLmHMFEzm0jYVS6vWTeAg8Bo9x.toString()
                } else if (falg == 3) {
                    userbean?.panNumber = it.UeFqjoQ5IDuu8B3Zabq0.toString()
                    userbean?.cardImg = it.BLmHMFEzm0jYVS6vWTeAg8Bo9x.toString()
                }
            } else {
            }
        })
    }
}