package com.lemon.now.ui.activity

import ToastUtils
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.OrientationEventListener
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.util.singleClick
import com.lemon.now.base.etx.view.CustomDialogFail
import com.lemon.now.base.etx.view.dismissLoadingExt2
import com.lemon.now.base.etx.view.showLoadingExt2
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityUserfacestepBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.model.HomeViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID


/**
 *   Lemon Cash
 *  UserFaceActivity.java
 *
 */
class UserFaceActivity : BaseActivity1<HomeViewModel, ActivityUserfacestepBinding>() {
    companion object {
        private const val REQUEST_CODE_PERMISSION_CAMERA = 100
    }

    private var imageCapture: ImageCapture? = null
    var url: String = "";
    var falg: Boolean = false
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }

        if (allPermissionsGranted()) {
            startCamera()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                UserFaceActivity.REQUEST_CODE_PERMISSION_CAMERA
            )
        }

        mViewBind.takcam.setOnClickListener {
            mViewBind.takcam.visibility = View.GONE
            mViewBind.txtop.visibility = View.INVISIBLE
            mViewBind.llTx.visibility = View.VISIBLE
            val adjustEvent = AdjustEvent(ApiService.face)
            Adjust.trackEvent(adjustEvent)
            takePhoto()
        }

        mViewBind.Retry.setOnClickListener {
            mViewBind.img.setImageBitmap(null)
            mViewBind.img.visibility = View.GONE
            mViewBind.previewView.visibility = View.VISIBLE
            mViewBind.takcam.visibility = View.VISIBLE
            mViewBind.txtop.visibility = View.INVISIBLE
            mViewBind.llTx.visibility = View.GONE
            falg = false

            val file = File(url)
            val deleted = file.delete()
            url = ""
        }
        mViewBind.Submit.singleClick {
            if (falg && !url.isEmpty()) {
                showLoadingExt2("loading")
                mViewModel.aws2()
            }
        }
        MyOrientationEventListener(this)
    }

    inner class MyOrientationEventListener(context: Context?) :
        OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            if (orientation != ORIENTATION_UNKNOWN) {
                mDeviceOrientation = normalize(orientation)
            }
        }

        private fun normalize(orientation: Int): Int {
            if (orientation > 315 || orientation <= 45) {
                return 0
            }
            if (orientation > 45 && orientation <= 135) {
                return 90
            }
            if (orientation <= 225) {
                return 180
            }
            return if (orientation > 225 && orientation <= 315) {
                270
            } else 0
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            cacheDir,
            "${
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(System.currentTimeMillis())
            }"
        )
        val oldFolder = File(photoFile, "${UUID.randomUUID()}.jpg")

        if (!oldFolder.exists()) {
            photoFile.mkdirs()
        }
        val metadata = ImageCapture.Metadata().apply {
            isReversedVertical = false
        }
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(oldFolder).setMetadata(metadata).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    var bitmap =
                        BitmapFactory.decodeFile(oldFolder.absolutePath)

                    val compressedImageData =
                        compressBitmapToByteArray(bitmap!!, 300 * 1024, oldFolder.absolutePath)
                    val newFolder = File(photoFile, "${UUID.randomUUID()}.jpg")
                    saveBitmapToFile(compressedImageData, newFolder)
//
                    oldFolder.delete()
                    var bitmap2 =
                        BitmapFactory.decodeFile(newFolder.absolutePath)
                    mViewBind.img.setImageBitmap(bitmap2)

                    mViewBind.img.visibility = View.VISIBLE
                    mViewBind.previewView.visibility = View.GONE
                    mViewBind.takcam.setBackgroundResource(R.mipmap.takok)
                    falg = true
                    url = newFolder.getAbsolutePath()

//                        Toast.makeText(this@CameraActivity, "sucess: $savedUri", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@UserFaceActivity,
                        "fail: ${exc.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun allPermissionsGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview =
                Preview.Builder().setTargetRotation(this.windowManager.defaultDisplay.rotation)
                    .build()

            preview.setSurfaceProvider(mViewBind.previewView.surfaceProvider)
            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetRotation(this.windowManager.defaultDisplay.rotation)
                .setOutputImageRotationEnabled(true)
                .build()
            imageCapture =
                ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .setTargetResolution(Size(250, 250)).build()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "fail: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }


    var mDeviceOrientation: Int = 0
    fun getJpegOrientation(naturalJpegOrientation: Int, deviceOrientation: Int): Int {
        return (naturalJpegOrientation + deviceOrientation) % 360
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
//        matrix.postScale(-1F, 1F);
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }

    fun compressBitmapToByteArray(bitmap: Bitmap, maxSize: Int, imagePath: String): ByteArray {

        val outputStream = ByteArrayOutputStream()
        val exifInterface = ExifInterface(imagePath)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.setScale(1f, -1f)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }

            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }

            ExifInterface.ORIENTATION_UNDEFINED -> {
                matrix.postScale(-1f, 1f)
            }
        }
        Log.d(
            "InstalledApp",
            "ID: $orientation person: "
        )
        var quality = 100
        val rotatedImage =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        rotatedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        while (outputStream.toByteArray().size > maxSize && quality > 0) {
            outputStream.reset()
            quality -= 5
            rotatedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        return outputStream.toByteArray()
    }


    fun saveBitmapToFile(imageData: ByteArray, outputFile: File) {
        try {
            val fileOutputStream = FileOutputStream(outputFile)
            fileOutputStream.write(imageData)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION_CAMERA -> {
                if (allPermissionsGranted()) {
                    Toast.makeText(
                        this,
                        " ${intent.getStringExtra("phototext")}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startCamera()
                } else {
                    finish()
                }
            }

        }
    }

    override fun createObserver() {


        mViewModel.commonData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val file = File(url)
                val credentials = BasicSessionCredentials(
                    it.ckERDTGankxlbEoezBJ47wlSl?.accessKeyId,
                    it?.ckERDTGankxlbEoezBJ47wlSl?.secretAccessKey,
                    it?.ckERDTGankxlbEoezBJ47wlSl?.sessionToken
                )
                val s3Client =
                    AmazonS3Client(credentials, Region.getRegion(it?.Jkm3b3aNx3Ef8B98NX))
                val bucketName = it?.dxpiLILEoy0bCkx
                val key = "${it?.q8vwEJMot33YpsV2okuZe67ym1Eh}${
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
                            val imageUrl = "${it?.ql04hxK6f7XCrL47zFHGrHl7STBwm98}/$key"
                            Log.i("S3", "COMPLETED ok" + imageUrl)
                            val map = hashMapOf(
                                "wAEOhq59d6LcUFrOZaUE5yaGHXT" to "liveness",
                                "gDkPqFyy6l9QF7Gj4" to key
                            )
                            mViewModel.faceStep(map)
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    }

                    override fun onError(id: Int, e: Exception) {
                        Log.e("S3", "error", e)
                        dismissLoading()
                        ToastUtils.showShort(this@UserFaceActivity, e?.message ?: "timeout")

                    }
                })
            }
        })

        mViewModel.userFaceStepdata.observe(this, Observer {
            dismissLoadingExt2()
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                setResult(RESULT_OK)
                finish()
            } else {
                ToastUtils.showShort(this@UserFaceActivity, it.vWCgp64OkxPVoGqics)
                val dialog = CustomDialogFail(this@UserFaceActivity)
                dialog.setConfirmCallback {
                }
                dialog.setCancelCallback {

                }
                dialog.setCancelable(false)
                dialog.show()
                dialog.setcontent("Upload failed, please try again.")
                val dialogWindow: Window = dialog.window!!
                val m: WindowManager = getWindowManager()
                val d = m.defaultDisplay
                val p = dialogWindow.attributes
                p.width = (d.width * 0.95).toInt()
                p.gravity = Gravity.CENTER
                dialogWindow.attributes = p
            }
        })
    }
}