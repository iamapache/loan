package com.lemon.now.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lemon.now.online.R
import com.lemon.now.ui.bean.VNozeoqh

/**
 *   Lemon Cash
 *  OrderAdapter.java
 *
 */
class OrderAdapter(private val listener: OnItemClickListener, private val context: Context, private val dataList: List<VNozeoqh>, var status: Int) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private val ITEM_VIEW_TYPE_EMPTY = 0
    private val ITEM_VIEW_TYPE_DATA = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_EMPTY -> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.empty_layout, parent, false)
                EmptyViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
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

        fun onFBClick(position: Int)
    }
    inner class DataViewHolder(itemView: View) : ViewHolder(itemView) {
        var orderfb = itemView.findViewById<ImageView>(R.id.orderfb)
        var productLogo = itemView.findViewById<ImageView>(R.id.productLogo)
        var ductName = itemView.findViewById<TextView>(R.id.ductName)
        var itemamount = itemView.findViewById<TextView>(R.id.itemamount)
        var itemdate = itemView.findViewById<TextView>(R.id.itemdate)
        var orderno = itemView.findViewById<TextView>(R.id.orderno)

        var tv_status = itemView.findViewById<TextView>(R.id.tv_status)

        init {
            orderfb.setOnClickListener {
                val position = adapterPosition
                listener.onFBClick(position)
            }

        }

        fun bind(data: VNozeoqh) {
            itemView.setOnClickListener {
                val position = adapterPosition
                listener.onItemClick(position)
            }
            Glide.with(context).load(data.pPQGUTNAxoA).into(productLogo)
            ductName.text =  data.zTAPvIwFI3Sv7UZv2SVGDrIOePGxxR9AqV
            itemamount.text = "₹ " +data.WA4R2qnu5lhz7.toString()
            itemdate.text = data.sktzZnR1tYbzNliF0ZUNLSQLWwz6g3hlyscpj
            orderno.text = data.sCVB4OFAaUm0Ba1V5nsjRGOuGvHSS8t
            when (status) {
                0 -> {
                    when (data.S1giqNNoozNFE8yRzARpD2VdSEkMd) {
                        1 -> {
                            tv_status.text = "Pending"
                            tv_status.setTextColor(Color.parseColor("#1FACFE"))
                        }
                        2 -> {
                            tv_status.text = "Disbursing"
                            tv_status.setTextColor(Color.parseColor("#37926B"))
                        }
                        3 -> {
                            tv_status.text = "To be Repaid"
                            tv_status.setTextColor(Color.parseColor("#F05C09"))
                        }
                        4 -> {
                            tv_status.text = "Repaid"
                            tv_status.setTextColor(Color.parseColor("#999999"))
                        }
                        5 -> {
                            if(data.a2kevgH5EWY9waHNv76F6xKXEwY==1){
                                tv_status.text = "Disbursing Fail"
                                tv_status.setTextColor(Color.parseColor("#F33CAC"))
                            }else{
                                tv_status.text = "Denied"
                                tv_status.setTextColor(Color.parseColor("#F33CAC"))
                            }

                        }
                        6 -> {
                            tv_status.text = "Overdue"
                            tv_status.setTextColor(Color.parseColor("#F9182D"))
                        }
                    }
                }
                1-> {
                    tv_status.text = "Pending"
                    tv_status.setTextColor(Color.parseColor("#1FACFE"))
                }
                2 -> {
                    tv_status.text = "Disbursing"
                    tv_status.setTextColor(Color.parseColor("#37926B"))
                }
                3 -> {
                    tv_status.text = "To be Repaid"
                    tv_status.setTextColor(Color.parseColor("#F05C09"))
                }
                4 -> {
                    tv_status.text = "Repaid"
                    tv_status.setTextColor(Color.parseColor("#999999"))
                }
                5 -> {
                    if(data.a2kevgH5EWY9waHNv76F6xKXEwY==1){
                        tv_status.text = "Disbursing Fail"
                        tv_status.setTextColor(Color.parseColor("#F33CAC"))
                    }else{
                        tv_status.text = "Denied"
                        tv_status.setTextColor(Color.parseColor("#F33CAC"))
                    }
                }
                6 -> {
                    tv_status.text = "Overdue"
                    tv_status.setTextColor(Color.parseColor("#F9182D"))
                }
            }
        }
    }

    inner class EmptyViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind() {
            // Bind empty state views
        }
    }
}