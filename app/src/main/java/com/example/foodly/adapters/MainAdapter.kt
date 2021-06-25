package com.example.foodly.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.foodly.R
import com.squareup.picasso.Picasso
import com.example.foodly.activities.DetailsActivity
import com.example.foodly.data.DataList
import com.example.foodly.database.CartData
import com.example.foodly.database.Cart_Items
import com.example.foodly.database.FavData
import com.example.foodly.database.Fav_res
import java.util.ArrayList

class MainAdapter(val context:Context, val list:ArrayList<DataList>):RecyclerView.Adapter<MainAdapter.View_Holder>(){

    class View_Holder(view:View):RecyclerView.ViewHolder(view){
        val img = view.findViewById<ImageView>(R.id.item_img)
        val name = view.findViewById<TextView>(R.id.item_name)
        val price = view.findViewById<TextView>(R.id.item_price)
        val rating = view.findViewById<TextView>(R.id.item_rating)
        val fav = view.findViewById<ImageView>(R.id.item_fav_icon)
        val item = view.findViewById<ConstraintLayout>(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_single_item,parent,false)
        return View_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.name.text = data.name
        holder.price.text = "₹ "+data.cost_for_one+"/person"
        holder.rating.text = data.rating
        Picasso.get().load(data.image_url).error(R.drawable.ic_icon).into(holder.img)
        val ett = Fav_res(data.name,data.image_url,data.id)
        if(FavData(context,ett,"getid").execute().get()!=null){
            holder.fav.tooltipText = "Remove From Favourites"
            holder.fav.setImageResource(R.drawable.ic_float_fav_clicked)
        }else{
            holder.fav.tooltipText = "Add to Favourites"
            holder.fav.setImageResource(R.drawable.ic_float_fav)
        }
        holder.item.setOnClickListener {
            val i =Intent(context, DetailsActivity::class.java)
            i.putExtra("id",data.id)
            i.putExtra("name",data.name)
            i.putExtra("url",data.image_url)
            Handler().postDelayed(Runnable {
                CartData(context, Cart_Items(0,"","","",""),"deleteall").execute().get()
                context.startActivity(i)
            },200)
        }
        holder.fav.setOnClickListener {
            if(holder.fav.tooltipText == "Add to Favourites"){
                if(FavData(context,ett,"insert").execute().get() as Boolean) {
                    holder.fav.tooltipText = "Remove From Favourites"
                    holder.fav.setImageResource(R.drawable.ic_float_fav_clicked)

                }
            }
            else{
                if(FavData(context,ett,"delete").execute().get() as Boolean) {
                holder.fav.tooltipText = "Add to Favourites"
                holder.fav.setImageResource(R.drawable.ic_float_fav)
            }
        }
    } }
}
