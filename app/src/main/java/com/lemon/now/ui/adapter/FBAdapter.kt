package com.lemon.now.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lemon.now.online.R
import com.lemon.now.ui.activity.FabackDetailActivity
import com.lemon.now.ui.bean.ATZAuA4z

/**
 *   Lemon Cash
 *  OrderAdapter.java
 *
 */
class FBAdapter(private val context: Context, private val dataList: List<ATZAuA4z>) :
    RecyclerView.Adapter<FBAdapter.ViewHolder>() {

    private val ITEM_VIEW_TYPE_EMPTY = 0
    private val ITEM_VIEW_TYPE_DATA = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EMPTY -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.empty_layout2, parent, false)
                EmptyViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_fb, parent, false)
                DataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DataViewHolder -> holder.bind(dataList[position])
            is EmptyViewHolder -> holder.bind()
        }
    }



    override fun getItemCount(): Int {
        return if (dataList.isEmpty()) 1 else dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList.isEmpty()) ITEM_VIEW_TYPE_EMPTY else ITEM_VIEW_TYPE_DATA
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    inner class DataViewHolder(itemView: View) : ViewHolder(itemView) {
        var productLogo = itemView.findViewById<ImageView>(R.id.productLogo)
        var ductName = itemView.findViewById<TextView>(R.id.ductName)
        var date = itemView.findViewById<TextView>(R.id.date)
        var ordernumber = itemView.findViewById<TextView>(R.id.ordernumber)
        var orderno = itemView.findViewById<TextView>(R.id.orderno)
        var count = itemView.findViewById<TextView>(R.id.count)
        init {

        }

        fun bind(data: ATZAuA4z) {
            itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("bean", data)
                intent.setClass(context, FabackDetailActivity::class.java)
                context.startActivity(intent)
            }
            Glide.with(context).load(data.pPQGUTNAxoA).into(productLogo)
            ductName.text =  data.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
            date.text =  data.K5FqwgrWtlhywzBHiAf4P7al
            ordernumber.text =  data.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t
            orderno.text =  data.OQjcAh53o829sGL0
            count.text =  data.XW0bEq4a5qYPTKfYJcH1wT2.toString()
        }
    }

    inner class EmptyViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind() {
            // Bind empty state views
        }
    }
}