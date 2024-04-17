package com.lemon.now.ui.activity

import ToastUtils
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.amazonaws.auth.BasicSessionCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.lemon.now.base.App
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.util.logv
import com.lemon.now.base.etx.view.CustomDialog2
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivitySubfbBinding
import com.lemon.now.ui.adapter.QuestionAdapter
import com.lemon.now.ui.bean.Question
import com.lemon.now.ui.model.OrderViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


/**
 *   Lemon Cash
 *  OrderListActivity.java
 *
 */
class FabackActivity : BaseActivity1<OrderViewModel, ActivitySubfbBinding>() , QuestionAdapter.OnItemClickListener{
    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 101
            private const val REQUEST_IMAGE_CAPTURE = 1
            private const val REQUEST_SELECT_IMAGE = 2
    }
    private val questionList = ArrayList<Question>()
    private lateinit var adapter: QuestionAdapter
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
        mViewBind.llFb.setOnClickListener {
            val map = hashMapOf(
                "gechsSbDxMXWr0ebMWvxyyMrfgsU6u4CEs" to 1,
                "oFAPPnaXGtjaefpW2ahcm" to 1
            )
            mViewModel.orderlist(map,true)
        }
        mViewBind.next.setOnClickListener {
            if(mViewBind.content.text.toString().isNullOrEmpty()){
                ToastUtils.showShort(this@FabackActivity,getString(R.string.toastinformation))
                return@setOnClickListener
        }
            if(mViewBind.fbtext.text.toString().isNullOrEmpty()){
                ToastUtils.showShort(this@FabackActivity,getString(R.string.toastinformation))
                return@setOnClickListener
            }
             val urlList = ArrayList<String>()
            for (question in questionList) {
                urlList.add(question.url)
            }
            val jsonString = Gson().toJson(urlList)
            val map = hashMapOf(
                "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to intent.getStringExtra("id").toString(),
                "OQjcAh53o829sGL0" to mViewBind.fbtext.text.toString(),
                "kTzkWYHHqvl" to mViewBind.content.text.toString(),
                "JFF8vDMjgOJHRNlj11" to jsonString.toString()
            )
            mViewModel.fbsave(map)
        }
        Glide.with(this).load(intent.getStringExtra("img")).into(mViewBind.productLogo)
        mViewBind.pfdroDCductName.text= intent.getStringExtra("name")
        mViewBind.orderid.text= intent.getStringExtra("id")
        mViewBind.recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = QuestionAdapter(this,questionList)
        mViewBind.recyclerView.adapter = adapter
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetView.findViewById<View>(R.id.takePhotoButton).setOnClickListener {
            dispatchTakePictureIntent()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<View>(R.id.selectPhotoButton).setOnClickListener {
            dispatchSelectImageIntent()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
    }
    private lateinit var currentPhotoPath: String
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
            currentPhotoPath?.logv()
        }
    }
    private fun dispatchSelectImageIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { selectPictureIntent ->
            selectPictureIntent.type = "image/*"
            startActivityForResult(selectPictureIntent, REQUEST_SELECT_IMAGE)
        }
    }
    private fun allPermissionsGranted(): Boolean {
        return ( ContextCompat.checkSelfPermission(
            this, permission
        )== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        )  == PackageManager.PERMISSION_GRANTED)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode == Activity.RESULT_OK ) {

            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {

                    val filePath: String? = currentPhotoPath
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
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    val compressedImageData = compressBitmapToByteArray(bitmap!!, 200 * 1024)
                    saveBitmapToFile(compressedImageData, oldFolder)

                    url = oldFolder.getAbsolutePath()
                    if (!url.isEmpty()) {
                        mViewModel.aws()
                    }
                }
                REQUEST_SELECT_IMAGE -> {
                    upudateImg(data)
                }
            }
        }
    }

    private fun upudateImg(data: Intent?) {
        val selectedImageUri: Uri? = data?.data
        val filePath: String? = getRealPathFromURI(selectedImageUri)
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
        val bitmap = BitmapFactory.decodeFile(filePath)
        val compressedImageData = compressBitmapToByteArray(bitmap!!, 200 * 1024)
        saveBitmapToFile(compressedImageData, oldFolder)

        url = oldFolder.getAbsolutePath()
        if (!url.isEmpty()) {
            mViewModel.aws()
        }
    }

    var url :String="";
    override fun createObserver() {
        mViewModel.orderdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val popupMenu = PopupMenu(this, mViewBind.fbtext)
                val customData = it.q9A6YHAPyW5fszMNtEkmJDOxEGjp2vqK6
                for (i in customData.indices) {
                    popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i].OQjcAh53o829sGL0)
                }
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    val selectedItem = customData[menuItem.order]
                    mViewBind.fbtext.text =selectedItem.OQjcAh53o829sGL0
                    ToastUtils.showShort(this@FabackActivity,selectedItem.LMKm3bmiY48uGs5y7SGxwxVFHpwj5qmn)
                    true
                }
                popupMenu.show()
            }
        })

        mViewModel.commondata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val dialog = CustomDialog2(this@FabackActivity)
                dialog.setCancelable(false)
                dialog.setConfirmCallback {
                    finish()
                }
                dialog.show()
            }
        })

        mViewModel.awsData.observe(this, Observer {
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
                            Log.i("S3", "上传完成，正在确认" + imageUrl)
                            questionList.add(Question(url, key))
                            mViewBind.recyclerView.adapter?.notifyDataSetChanged()
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                        Log.i("S3", "上传进度：$bytesCurrent/$bytesTotal")
                    }

                    override fun onError(id: Int, e: Exception) {
                        Log.e("S3", "上传出错", e)
                    }
                })
            }
        })
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        var filePath: String? = null
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver = contentResolver
        val cursor = contentResolver.query(contentUri!!, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            filePath = cursor.getString(columnIndex)
            cursor.close()
        }
        return filePath
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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSION_REQUEST_CODE -> {
                if (allPermissionsGranted()) {
                    showBottomSheetDialog()
                } else {
                    finish()
                }
            }

        }
    }

    override fun onItemClick(position: Int) {
        if (allPermissionsGranted()) {
            if(questionList.size<9){
                showBottomSheetDialog()
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf( Manifest.permission.CAMERA,
                    permission
                ), SMS_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun delete(position: Int) {
        position.toString().logv()
        questionList.removeAt(position)
        mViewBind.recyclerView.adapter?.notifyDataSetChanged()
    }
}