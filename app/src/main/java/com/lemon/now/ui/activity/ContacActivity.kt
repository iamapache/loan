package com.lemon.now.ui.activity

import ToastUtils
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityAuthcontacBinding
import com.lemon.now.ui.bean.AuthInfoBean
import com.lemon.now.ui.model.AuthModel
import org.json.JSONArray
import org.json.JSONObject

/**
 *   Lemon Cash
 *  ContacActivity.java
 *
 */
class ContacActivity : BaseActivity1<AuthModel, ActivityAuthcontacBinding>() {
    private var nameEditText: EditText? = null
    private var relationEditText: TextView? = null
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mViewModel.authinfo()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST_CODE
            )
        }
        var contactNum = intent.getIntExtra("contactNum",2)

        for (i in 1 .. contactNum) {
            addRowLayout()
        }

    }

    override fun createObserver() {

        mViewModel.authinfoData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                extracted(it)
                mViewBind.add.setOnClickListener {view ->
                    addRowLayout()
                    extracted(it)
                }

                mViewBind.next.setOnClickListener {view ->
                    val jsonArray = JSONArray()
                    for (i in 0 until mViewBind.containerLayout.childCount) {
                        val childView = mViewBind.containerLayout.getChildAt(i)
                        if (childView is LinearLayout) {
                            val linearLayout = childView as LinearLayout
                            val number = linearLayout.findViewById<TextView>(R.id.number)
                            val name = linearLayout.findViewById<EditText>(R.id.name)
                            val pick = linearLayout.findViewById<ImageView>(R.id.pick)
                            val relation = linearLayout.findViewById<TextView>(R.id.relation)

                            if (number.text.toString().isNotEmpty() && name.text.toString()
                                    .isNotEmpty()
                                && relation.text.toString().isNotEmpty()
                            ) {
                                val jsonObject = JSONObject()
                                jsonObject.put("contactName", name.text.toString())
                                jsonObject.put("contactPhone", number.text.toString())
                                jsonObject.put("contactRelation", relation.text.toString())
                                jsonArray.put(jsonObject)

                            } else {
                                ToastUtils.showShort(this@ContacActivity, "Please fill in all the information")
                                return@setOnClickListener
                            }
                        }

                    }
                    if (jsonArray.toString().length>5) {

                        val map = hashMapOf(
                            "ZbspMUi4YGmGq0ioNH6mrzQcHCsGrz883Ed" to "4",
                            "kFhzAgCSnIpIsJD" to jsonArray.toString()
                        )

                        mViewModel.submitAuth(map)
                    }
                }
            }
        })

        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val intent = intent
                intent.setClass(this@ContacActivity, BankInfoActivity::class.java)
                startActivity(intent)
            }else{
                ToastUtils.showShort(this@ContacActivity,it.vWCgp64OkxPVoGqics)
            }
        })
    }

    private fun ContacActivity.extracted(it: AuthInfoBean) {
        for (i in 0 until mViewBind.containerLayout.childCount) {
            val childView = mViewBind.containerLayout.getChildAt(i)
            if (childView is LinearLayout) {
                val linearLayout = childView as LinearLayout
                val number = linearLayout.findViewById<TextView>(R.id.number)
                val name = linearLayout.findViewById<EditText>(R.id.name)
                val pick = linearLayout.findViewById<ImageView>(R.id.pick)
                val relation = linearLayout.findViewById<TextView>(R.id.relation)
                val ll_relation = linearLayout.findViewById<LinearLayout>(R.id.ll_relation)
                ll_relation.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    val customData = it.mwc9FRGzdTrCkk1bYAHy0
                    for (i in customData.indices) {
                        popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                    }
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        var selectedItem = customData[menuItem.order]
                        relation.setText(selectedItem)
                        true
                    }
                    popupMenu.show()
                }

                pick.setOnClickListener { view ->
                    nameEditText = name
                    relationEditText = number
                    val intent =
                        Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                    startActivityForResult(intent, CONTACT_PICKER_REQUEST)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mViewModel.authinfo()
            } else {
                finish()
            }
        }
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val contactData = data?.data ?: return
            val cursor = contentResolver.query(contactData, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    nameEditText?.setText(name)
                    val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                    val phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )
                    phoneCursor?.use { phoneCursor ->
                        if (phoneCursor.moveToFirst()) {
                            val phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            relationEditText?.setText(phoneNumber)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val CONTACT_PICKER_REQUEST = 1001
        private const val READ_CONTACTS_PERMISSION_REQUEST_CODE = 101
    }

    private fun addRowLayout() {
        val inflater = LayoutInflater.from(this)
        val rowLayout = inflater.inflate(R.layout.row_layout, mViewBind.containerLayout, false)
        mViewBind.containerLayout.addView(rowLayout)
    }

}