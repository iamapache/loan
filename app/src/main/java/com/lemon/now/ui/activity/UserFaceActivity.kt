package com.lemon.now.ui.activity

import ToastUtils
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityUserfacestepBinding
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
        private const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 101
    }
    private var imageCapture: ImageCapture? = null
    var url :String="";
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
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                UserFaceActivity.REQUEST_CODE_PERMISSION_CAMERA
            )
        }

        mViewBind.takcam.setOnClickListener {
            mViewBind.takcam.visibility=View.GONE
            mViewBind.txtop.visibility=View.INVISIBLE
            mViewBind.llTx.visibility=View.VISIBLE
            takePhoto()
        }

        mViewBind.Retry.setOnClickListener {
            mViewBind.img.setImageBitmap(null)
            mViewBind.img.visibility = View.GONE
            mViewBind.previewView.visibility = View.VISIBLE
            mViewBind.takcam.visibility=View.VISIBLE
            mViewBind.txtop.visibility=View.INVISIBLE
            mViewBind.llTx.visibility=View.GONE
            falg = false

            val file = File(url)
            val deleted = file.delete()
            url=""
        }
        mViewBind.Submit.setOnClickListener {
            if (falg&&!url.isEmpty()) {
                mViewModel.aws()
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            externalMediaDirs.firstOrNull(),
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
        if (allPermissionsGranted()) {
            val outputOptions = ImageCapture.OutputFileOptions.Builder(oldFolder).build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        var bitmap: Bitmap? =null

                            bitmap =
                                BitmapFactory.decodeFile(oldFolder.absolutePath)
                        val compressedImageData = compressBitmapToByteArray(bitmap!!, 200 * 1024)
                        val newFolder = File(photoFile, "${UUID.randomUUID()}.jpg")
                        saveBitmapToFile(compressedImageData, newFolder)
                        oldFolder.delete()

                        mViewBind.img.visibility = View.VISIBLE
                        mViewBind.previewView.visibility = View.GONE
                        mViewBind.takcam.setBackgroundResource(R.mipmap.takok)
                        falg = true
                        val bitmap2 = BitmapFactory.decodeFile(newFolder.absolutePath)
                        mViewBind.img.setImageBitmap(bitmap2)
                        url =newFolder.getAbsolutePath()

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
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        }
    }
    private fun allPermissionsGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(mViewBind.previewView.surfaceProvider)

            imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setTargetRotation(mViewBind.previewView.display.rotation).setTargetResolution(Size(500,500)).build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "fail: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun compressBitmapToByteArray(bitmap: Bitmap, maxSize: Int): ByteArray {
        val outputStream = ByteArrayOutputStream()

        var quality = 100
        val matrix = Matrix()
        matrix.postRotate(270F)
        val rotatedImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        rotatedImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        while (outputStream.toByteArray().size > maxSize && quality > 0) {
            outputStream.reset()
            quality -= 10
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
                    Toast.makeText(this, " ${intent.getStringExtra("phototext")}", Toast.LENGTH_SHORT).show()
                    startCamera()
                } else {
                    finish()
                }
            }

            REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
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
                    }
                })
            }
        })

        mViewModel.userFaceStepdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                setResult(RESULT_OK)
                finish()
            }else {
                ToastUtils.showShort(this@UserFaceActivity, it.vWCgp64OkxPVoGqics)
            }
        })
    }
}