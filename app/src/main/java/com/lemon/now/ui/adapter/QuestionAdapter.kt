package com.lemon.now.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lemon.now.online.R
import com.lemon.now.ui.bean.Question

/**
 *   Lemon Cash
 *  QuestionAdapter.java
 *
 */
class QuestionAdapter(private val listener: OnItemClickListener,private val questionList: ArrayList<Question>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_DEFAULT = 0
    private val VIEW_TYPE_NORMAL = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DEFAULT) {
            val defaultView = LayoutInflater.from(parent.context).inflate(R.layout.item_default_question, parent, false)
            DefaultViewHolder(defaultView)
        } else {
            val normalView = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
            ViewHolder(normalView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val question = questionList[position - 1]
            val bitmap = BitmapFactory.decodeFile(question.path)
            holder.img.setImageBitmap(bitmap)


        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun delete(position: Int)
    }
    override fun getItemCount() = questionList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_DEFAULT else VIEW_TYPE_NORMAL
    }

    inner class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img=  itemView.findViewById<ImageView>(R.id.img)
        var delete=  itemView.findViewById<ImageView>(R.id.delete)
        init {

            delete.setOnClickListener {
                val position = adapterPosition
                listener.delete(position-1)
            }
        }
    }
}
