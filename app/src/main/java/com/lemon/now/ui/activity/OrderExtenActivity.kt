package com.lemon.now.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lemon.now.base.activity.BaseActivity1
import com.lemon.now.base.etx.view.CustomDialog
import com.lemon.now.online.databinding.ActivityOrderextenBinding
import com.lemon.now.ui.bean.M7CdaEiz0WPh1Cs3iyzkg6Od
import com.lemon.now.ui.model.OrderViewModel

/**
 *   Lemon Cash
 *  OrderListActivity.java
 *
 */
class OrderExtenActivity : BaseActivity1<OrderViewModel, ActivityOrderextenBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        var bean = intent.getParcelableExtra<M7CdaEiz0WPh1Cs3iyzkg6Od>("bean")
        mViewBind.back.setOnClickListener {
            finish()
        }



        mViewBind.extension.setOnClickListener {
            val dialog = CustomDialog(this@OrderExtenActivity)
            dialog.setCancelable(false)
            dialog.setConfirmCallback {
                val map = hashMapOf(
                    "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to bean?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t.toString(),
                    "cUVxr56uydpK5vG1LAkDeCGOAj4gfkDx" to "extend","obhH38I5kIj71jbNKPzEFrKkelMHT" to bean?.obhH38I5kIj71jbNKPzEFrKkelMHT.toString()
                )
                mViewModel.repay(map)
            }
            dialog.setCancelCallback {

            }
            dialog.show()
            dialog.setTitle("TIPS")
            dialog.setcontent("By paying a nominal extension fee, you can settle the entire bill at a later time.")
            val dialogWindow: Window = dialog.window!!
            val m: WindowManager = getWindowManager()
            val d = m.defaultDisplay
            val p = dialogWindow.attributes
            p.width = (d.width * 0.95).toInt()
            p.gravity = Gravity.CENTER
            dialogWindow.attributes = p

        }
        val map = hashMapOf(
            "sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t" to bean?.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t.toString()
        )
        mViewModel.extendetail(map)
    }
    override fun onRestart() {
        super.onRestart()

        finish()
    }

    override fun createObserver() {

        mViewModel.payresultdata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
                if (it.w9VJPY1aYx65wYI2JTt4s == 0) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(it.aWptfFdiAKe6gU8KTnX6y6VfmFieTcLTd))
                    startActivity(intent)
                } else if (it.w9VJPY1aYx65wYI2JTt4s == 1) {
                    val intent = Intent(this@OrderExtenActivity, WebActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("url", it.aWptfFdiAKe6gU8KTnX6y6VfmFieTcLTd)
                    startActivity(intent)
                }
            }
        })

        mViewModel.extendata.observe(this, Observer {
            if (it.rZ81DSU7WU4hny4ukGHljvjO41bfB == 1) {
               var bean =it
                Glide.with(this).load(bean?.pPQGUTNAxoA).into(mViewBind.productLogo)
                mViewBind.pfdroDCductName.text =  bean?.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV

                mViewBind.amount.text = "₹ " +  bean?.lKKVSauI0hrmQmhzt6A.toString()
                mViewBind.date.text =  bean?.JQf1oh1cEBZVVL5yUx.toString()+" Days"

                mViewBind.receivedamount.text =bean?.IPpbpFEJ5.toString()
                mViewBind.receivedate.text =   "₹ " +  bean?.Ei5rFw3ggCfFFxvogcCvdtX.toString()
            }
        })
    }
}