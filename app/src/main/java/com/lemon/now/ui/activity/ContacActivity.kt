package com.lemon.now.ui.activity

import ToastUtils
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.view.CustomDialog
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityAuthcontacBinding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.bean.AuthInfoBean
import com.lemon.now.ui.bean.Contactbean
import com.lemon.now.ui.model.AuthModel
import com.lemon.now.ui.model.MessageEvent
import com.lemon.now.util.SettingUtil
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type

/**
 *   Lemon Cash
 *  ContacActivity.java
 *
 */
class ContacActivity : BaseActivity1<AuthModel, ActivityAuthcontacBinding>() {
    private var nameEditText: EditText? = null
    private var relationEditText: TextView? = null
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.back.setOnClickListener {
            finish()
        }

        mViewModel.authinfo()


        someActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    mViewModel.getuserinfo(
                        SettingUtil.isVpnConnected(this@ContacActivity).toString(),
                        SettingUtil.getAvailableSimSlots(this@ContacActivity).toString(),
                        SettingUtil.getActivatedSimCount(this@ContacActivity).toString()
                    )
                }
            }
    }

    private var liveness: Boolean = false
    override fun createObserver() {

        mViewModel.authinfoData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {

                if(!it.kFhzAgCSnIpIsJD.isNullOrEmpty()){
                    val listType: Type = object : TypeToken<List<Contactbean>>() {}.type
                    val gson = Gson()
                    val list: List<Contactbean> = gson.fromJson(it.kFhzAgCSnIpIsJD, listType)
                    if (it.kFhzAgCSnIpIsJD.length > 5) {

                        for (i in 1..list.size) {
                            addRowLayout()
                        }
                    } else {
                        for (i in 1..it.PLnVmUR) {
                            addRowLayout()
                        }
                    }
                    extracted(list, it)
                }else{
                        for (i in 1..it.PLnVmUR) {
                            addRowLayout()
                        }
                    extracted2(it)
                }


                mViewBind.add.setOnClickListener { view ->
                    addRowLayout()
                    extracted2(it)
                }

                mViewBind.next.setOnClickListener { view ->
                    val jsonArray = JSONArray()
                    val adjustEvent = AdjustEvent(ApiService.step4)
                    Adjust.trackEvent(adjustEvent)
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
                                ToastUtils.showShort(
                                    this@ContacActivity,
                                    "Please fill in all the information"
                                )
                                return@setOnClickListener
                            }
                        }

                    }
                    if (jsonArray.toString().length > 5) {
                        val dialog = CustomDialog(this@ContacActivity)
                        dialog.setCancelable(false)
                        dialog.setConfirmCallback {
                            val map = hashMapOf(
                                "ZbspMUi4YGmGq0ioNH6mrzQcHCsGrz883Ed" to "4",
                                "kFhzAgCSnIpIsJD" to jsonArray.toString()
                            )
                            mViewModel.submitAuth(map)
                        }
                        dialog.setCancelCallback {
                        }
                        dialog.show()
                        dialog.setTitle("TIPS")
                        dialog.setcontent("Please note: Apart from bank card details, no other information can be modified once submitted. Ensure all information is accurate before proceeding with your submission.")
                        val dialogWindow: Window = dialog.window!!
                        val m: WindowManager = getWindowManager()
                        val d = m.defaultDisplay
                        val p = dialogWindow.attributes
                        p.width = (d.width * 0.95).toInt()
                        p.gravity = Gravity.CENTER
                        dialogWindow.attributes = p
                    }
                }
            }
        })

        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                mViewModel.getuserinfo(
                    SettingUtil.isVpnConnected(this@ContacActivity).toString(),
                    SettingUtil.getAvailableSimSlots(this@ContacActivity).toString(),
                    SettingUtil.getActivatedSimCount(this@ContacActivity).toString()
                )
//                EventBus.getDefault().post(MessageEvent(MessageEvent.au,""))

            } else {
                ToastUtils.showShort(this@ContacActivity, it.vWCgp64OkxPVoGqics)
            }
        })
        mViewModel.homebean.observe(this, Observer {
            dismissLoading()
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {

                if (it.FUiNUblg6avFY2 == 0) {
                    val dialog = CustomDialog(this@ContacActivity)
                    dialog.setConfirmCallback {
                        val intent = Intent(this@ContacActivity, UserFaceActivity::class.java)
                        intent.putExtra("phototext", "")
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
                } else {
                    if (it.Qbnsde5LgABnpY9IpFTFXkgR3l8 != null) {
                        val intent = Intent(this@ContacActivity, OrderActivity::class.java)
                        intent.putExtra("id", it.Qbnsde5LgABnpY9IpFTFXkgR3l8.nJNb2VY6)
                        intent.putExtra("bean", it.Qbnsde5LgABnpY9IpFTFXkgR3l8)
                        startActivity(intent)
                        EventBus.getDefault().post(MessageEvent(MessageEvent.finish, ""))
                        finish()
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            } else {
                ToastUtils.showShort(this@ContacActivity, it.vWCgp64OkxPVoGqics)
            }
        })

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contactUri: Uri = result.data?.data ?: return@registerForActivityResult
                val projection: Array<String> = arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                )
                contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val nameIndex =
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        val displayName = cursor.getString(nameIndex)
                        val phoneIndex =
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val phoneNumber = cursor.getString(phoneIndex)
                        nameEditText?.setText(displayName)
                        relationEditText?.setText(phoneNumber)
                    }
                }
            }
        }

    private fun ContacActivity.extracted(list: List<Contactbean>, it: AuthInfoBean) {
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
                if (list.size > 0 && list[i].contactPhone.isNotEmpty() && list[i].contactName.isNotEmpty() && list[i].contactRelation.isNotEmpty()) {
                    number.setText(list[i].contactPhone)
                    name.setText(list[i].contactName)
                    relation.setText(list[i].contactRelation)
                }

                pick.setOnClickListener { view ->
                    nameEditText = name
                    relationEditText = number
                    val intent =
                        Intent(
                            Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        )
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private fun ContacActivity.extracted2(it: AuthInfoBean) {
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
                        Intent(
                            Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        )
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private fun addRowLayout() {
        val inflater = LayoutInflater.from(this)
        val rowLayout = inflater.inflate(R.layout.row_layout, mViewBind.containerLayout, false)
        mViewBind.containerLayout.addView(rowLayout)
    }

}