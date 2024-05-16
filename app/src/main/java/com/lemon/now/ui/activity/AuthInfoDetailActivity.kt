package com.lemon.now.ui.activity

import ToastUtils
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.lemon.now.base.activity.BaseActivity
import com.lemon.now.online.R
import com.lemon.now.online.databinding.ActivityAuthinfo2Binding
import com.lemon.now.ui.ApiService
import com.lemon.now.ui.bean.Userbean
import com.lemon.now.ui.model.AuthModel
import com.lemon.now.ui.model.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.roundToInt

/**
 *   Lemon Cash
 *  AuthenticationActivity.java
 *
 */
class AuthInfoDetailActivity : BaseActivity<AuthModel, ActivityAuthinfo2Binding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.back.setOnClickListener {
            finish()
        }

        mViewModel.authinfo()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun updateTextViewPosition(progress: Int) {
        val layoutParams = mDatabind.textViewProgress.layoutParams as RelativeLayout.LayoutParams
        val progressWidth = mDatabind.progressBar.width
        val textViewWidth = mDatabind.textViewProgress.width
        val marginLeft = progress * progressWidth / mDatabind.progressBar.max - textViewWidth / 2
        layoutParams.leftMargin = marginLeft
        mDatabind.textViewProgress.layoutParams = layoutParams
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {
        if (messageEvent.getStatus() == MessageEvent.finish) {
            finish()
        }
    }
    private var contactNum: Int = 2

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createObserver() {

        mViewModel.authinfoData.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {

                mDatabind.education.setText( it.RgIwwGuM6dYSN4t23O0GPn8zst92eIaqZRwC.toString())
                mDatabind.marriage.setText( it.xfURXA8BaCLrizxNEQwguL4cPlJN7.toString())
                mDatabind.monthly.text = it.gclNe1gaPMSU0WGkMck.toString()
                mDatabind.work.text = it.zswPxseFJ4wfFRWEW.toString()
                mDatabind.industry.text = it.AYKOEPY4e0zKPzfrsKvZNxN2o.toString()
                mDatabind.whatsapp.setText( it.vJ66bDlFvxStgaxLp.toString())
                mDatabind.mail.setText( it.ZheSOnZb6V2S1InXqk69.toString())
                mDatabind.facebook.setText( it.jtwU2xNJjC.toString())
                mDatabind.textViewProgress.text = "₹ " + it.RUagIHMvlGBEDR.toString()

                if(!it.RUagIHMvlGBEDR.isNullOrEmpty()){
                    mDatabind.progressBar.progress= it.RUagIHMvlGBEDR.toInt()
                }


                contactNum = it.PLnVmUR
                mDatabind.progressBar.progress = (it.sxaJHj03AOxHpo2O47qKyr / it.SvcYq6jLm12) / 100
                mDatabind.progressBar.max = it.SvcYq6jLm12
                mDatabind.progressBar.min = it.sxaJHj03AOxHpo2O47qKyr
                mDatabind.progressBar.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        val roundedProgress = (progress.toFloat() / 100).roundToInt() * 100
                        seekBar?.progress = roundedProgress
                        mDatabind.textViewProgress.text = "₹ " + roundedProgress.toString()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
                mDatabind.priceMax.text = "₹ " + it.SvcYq6jLm12.toString()

                mDatabind.mail.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val input = s.toString()
                        val regex = Regex("[a-zA-Z0-9@-]+")
                        if (!regex.matches(input)) {
                        }
                    }
                })
                mDatabind.llEducation.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    val customData = it.KIAExUbtXJvHqO1eIAfctHzjRlHAyI
                    for (i in customData.indices) {
                        popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                    }
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        val selectedItem = customData[menuItem.order]
                        mDatabind.education.text = selectedItem
                        true
                    }
                    popupMenu.show()
                }
                mDatabind.education.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString().isNotEmpty()) {
                            val popupMenu = PopupMenu(this@AuthInfoDetailActivity, mDatabind.llMarriage)
                            val customData = it.DocSwVPKpef7F0Qbwtp6YmPjAoTIZBsJiF
                            for (i in customData.indices) {
                                popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                            }
                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                val selectedItem = customData[menuItem.order]
                                mDatabind.marriage.text = selectedItem
                                true
                            }
                            popupMenu.show()
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
                mDatabind.llMarriage.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    val customData = it.DocSwVPKpef7F0Qbwtp6YmPjAoTIZBsJiF
                    for (i in customData.indices) {
                        popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                    }
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        val selectedItem = customData[menuItem.order]
                        mDatabind.marriage.text = selectedItem
                        true
                    }
                    popupMenu.show()
                }
                mDatabind.marriage.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString().isNotEmpty()) {
                            val popupMenu = PopupMenu(this@AuthInfoDetailActivity, mDatabind.llMonthly)
                            val customData = it.nVMbOkJFW01L2ujozEHSQ6
                            for (i in customData.indices) {
                                popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                            }
                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                val selectedItem = customData[menuItem.order]
                                mDatabind.monthly.text = selectedItem
                                true
                            }
                            popupMenu.show()
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })

                mDatabind.llMonthly.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    val customData = it.nVMbOkJFW01L2ujozEHSQ6
                    for (i in customData.indices) {
                        popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                    }
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        val selectedItem = customData[menuItem.order]
                        mDatabind.monthly.text = selectedItem
                        true
                    }
                    popupMenu.show()
                }

                mDatabind.monthly.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString().isNotEmpty()) {
                            val popupMenu = PopupMenu(this@AuthInfoDetailActivity, mDatabind.llWork)
                            val customData = it.KfJ3zvyekom0HCZ0rH
                            for (i in customData.indices) {
                                popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                            }
                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                val selectedItem = customData[menuItem.order]
                                mDatabind.work.text = selectedItem
                                true
                            }
                            popupMenu.show()
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })

                mDatabind.llWork.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    val customData = it.KfJ3zvyekom0HCZ0rH
                    for (i in customData.indices) {
                        popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                    }
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        val selectedItem = customData[menuItem.order]
                        mDatabind.work.text = selectedItem
                        true
                    }
                    popupMenu.show()
                }
                mDatabind.work.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString().isNotEmpty()) {
                            val popupMenu = PopupMenu(this@AuthInfoDetailActivity, mDatabind.llIndustry)
                            val customData = it.nXN5n9vv
                            for (i in customData.indices) {
                                popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                            }
                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                val selectedItem = customData[menuItem.order]
                                mDatabind.industry.text = selectedItem
                                true
                            }
                            popupMenu.show()
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })

                mDatabind.llIndustry.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    val customData = it.nXN5n9vv
                    for (i in customData.indices) {
                        popupMenu.menu.add(Menu.NONE, Menu.NONE, i, customData[i])
                    }
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        val selectedItem = customData[menuItem.order]
                        mDatabind.industry.text = selectedItem
                        true
                    }
                    popupMenu.show()
                }



                mDatabind.next.setOnClickListener {
                    val adjustEvent = AdjustEvent(ApiService.step3)
                    Adjust.trackEvent(adjustEvent)
                    if (mDatabind.education.text.toString()
                            .isNotEmpty() && mDatabind.marriage.text.toString()
                            .isNotEmpty()
                        && mDatabind.monthly.text.toString().isNotEmpty()
                        && mDatabind.industry.text.toString()
                            .isNotEmpty() && mDatabind.work.text.toString().isNotEmpty()
                        && mDatabind.whatsapp.text.toString()
                            .isNotEmpty() && mDatabind.mail.text.toString().isNotEmpty()

                    ) {
                        var userbean = intent.getParcelableExtra<Userbean>("userbean")
                        val map = hashMapOf(
                            "ZbspMUi4YGmGq0ioNH6mrzQcHCsGrz883Ed" to "3",
                            "JagPyb82zgGT3vxOUXzqsvYoli410PMB" to userbean?.frontimg!!,
                            "aAMghzlwvAEFgDMSGe0MXdh2tyT6ZQTfnD9" to userbean?.backimg!!,
                            "xfURXA8BaCLrizxNEQwguL4cPlJN7" to mDatabind.marriage.text.toString(),
                            "RgIwwGuM6dYSN4t23O0GPn8zst92eIaqZRwC" to mDatabind.education.text.toString(),
                            "AYKOEPY4e0zKPzfrsKvZNxN2o" to mDatabind.industry.text.toString(),
                            "zswPxseFJ4wfFRWEW" to mDatabind.work.text.toString(),
                            "gclNe1gaPMSU0WGkMck" to mDatabind.monthly.text.toString(),
                            "ZheSOnZb6V2S1InXqk69" to mDatabind.mail.text.toString(),
                            "vJ66bDlFvxStgaxLp" to mDatabind.whatsapp.text.toString()
                        )
                        if (mDatabind.facebook.text.toString()
                                .isNotEmpty()
                        ) {
                            map.put("jtwU2xNJjC", mDatabind.facebook.text.toString())
                        }
                        if (mDatabind.progressBar.progress.toFloat().toInt() != 0) {
                            map.put(
                                "RUagIHMvlGBEDR",
                                mDatabind.progressBar.progress.toInt().toString()
                            )
                        }

                        mViewModel.submitAuth(map)
                    } else {
                        ToastUtils.showShort(
                            this@AuthInfoDetailActivity,
                            getString(R.string.toastinformation)
                        )
                    }
                }
            } else {
            }
        })

        mViewModel.submitdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                val intent = intent
                intent.putExtra("contactNum", contactNum)
                intent.setClass(this@AuthInfoDetailActivity, ContacActivity::class.java)
                startActivity(intent)
            } else {
                ToastUtils.showShort(this@AuthInfoDetailActivity, it.vWCgp64OkxPVoGqics)
            }
        })
    }

}