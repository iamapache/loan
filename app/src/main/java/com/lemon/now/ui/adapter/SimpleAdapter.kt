package com.lemon.now.ui.adapter

/**
 *   Lemon Cash
 *  SimpleAdapter.java
 *
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lemon.now.online.R

class SimpleAdapter(private val context: Context, private val data: List<String>) :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: ImageView = view.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data[position]).into(holder.textView)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
