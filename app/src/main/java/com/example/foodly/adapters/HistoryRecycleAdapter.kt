package com.example.foodly.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodly.R
import com.example.foodly.data.food_items
import java.util.ArrayList

class HistoryRecycleAdapter(val context: Context, val list:ArrayList<food_items>):RecyclerView.Adapter<HistoryRecycleAdapter.View_Holder>() {

    class View_Holder(view:View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.ord_item_item_name)
        val price: TextView = view.findViewById<TextView>(R.id.ord_item_item_price)
        val image = view.findViewById<ImageView>(R.id.ord_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return View_Holder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.name.text = data.food_name
        holder.price.text = data.price
        Glide.with(holder.image.context)
            .load(R.drawable.ic_icon)
            .into(holder.image)
    }
}