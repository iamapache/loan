package com.lemon.now.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
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
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.viewmodel.BaseViewModel
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityCameraBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID


/**
 *   Lemon Cash
 *  CameraActivity.java
 *
 */
class CameraActivity : BaseActivity1<BaseViewModel, ActivityCameraBinding>() {
    companion object {
        private const val REQUEST_CODE_PERMISSION_CAMERA = 100
    }

    private var imageCapture: ImageCapture? = null
    var falg: Boolean = false
    override fun initView(savedInstanceState: Bundle?) {

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_PERMISSION_CAMERA
            )
        }

        mViewBind.takcam.setOnClickListener {
            if (falg&&!url.isEmpty()) {
                val data = url
                val intent = Intent()
                intent.putExtra("url", data)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                takePhoto()
            }
        }
        mViewBind.taktry.setOnClickListener {
            mViewBind.img.setImageBitmap(null)
            mViewBind.img.visibility = View.GONE
            mViewBind.previewView.visibility = View.VISIBLE
            mViewBind.takcam.setBackgroundResource(R.mipmap.takcam)
            falg = false

            val file = File(url)
            val deleted = file.delete()
            url=""
        }
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

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(mViewBind.previewView.surfaceProvider)

            imageCapture = ImageCapture.Builder().setTargetResolution(Size(800,500)).build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "fail: ${exc.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }
    var url :String="";
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
        if (allPermissionsGranted()) {
            val outputOptions = ImageCapture.OutputFileOptions.Builder(oldFolder).build()
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                        var   bitmap =
                                BitmapFactory.decodeFile(oldFolder.absolutePath)
                        mViewBind.img.setImageBitmap(bitmap)
                        val compressedImageData = compressBitmapToByteArray(bitmap!!, 300 * 1024)
                        val newFolder = File(photoFile, "${UUID.randomUUID()}.jpg")
                        saveBitmapToFile(compressedImageData, newFolder)
                        oldFolder.delete()

                        mViewBind.img.visibility = View.VISIBLE
                        mViewBind.previewView.visibility = View.GONE
                        mViewBind.takcam.setBackgroundResource(R.mipmap.takok)
                        falg = true
//                        val bitmap2 = BitmapFactory.decodeFile(newFolder.absolutePath)
                        url =newFolder.getAbsolutePath()
                    }

                    override fun onError(exc: ImageCaptureException) {
                        Toast.makeText(
                            this@CameraActivity,
                            "fail: ${exc.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
        }
    }
    fun compressBitmapToByteArray(bitmap: Bitmap, maxSize: Int): ByteArray {
        val outputStream = ByteArrayOutputStream()
        var quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        while (outputStream.toByteArray().size > maxSize && quality > 0) {
            outputStream.reset()
            quality -= 5
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
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
                    startCamera()
                } else {
                    finish()
                }
            }

        }
    }
}