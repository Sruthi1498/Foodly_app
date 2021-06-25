package com.example.foodly.adapters

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodly.R

import com.example.foodly.data.RseList
import com.example.foodly.database.CartData

import com.example.foodly.database.Cart_Items
import java.lang.Exception
import java.util.ArrayList

class RestaurantAdapter(val context:Context, val list:ArrayList<RseList>, val res_name:String, val btn:Button):RecyclerView.Adapter<RestaurantAdapter.View_Holder>() {


    class View_Holder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.res_item_image)
        val name = view.findViewById<TextView>(R.id.res_item_name)
        val price = view.findViewById<TextView>(R.id.res_item_price)
        val add_btn = view.findViewById<Button>(R.id.res_item_add_btn)
        val item = view.findViewById<ConstraintLayout>(R.id.res_item)
        val inc = view.findViewById<LinearLayout>(R.id.inc_linear)
        val t1 = view.findViewById<Button>(R.id.t1)
        val t2 = view.findViewById<TextView>(R.id.t2)
        val t3 = view.findViewById<Button>(R.id.t3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_dish, parent, false)
            btn.setText("No items Selected")
        return View_Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val data = list[position]
        holder.name.text = data.name
        holder.price.text = "₹ " + data.cost_for_one + "/-"
        Glide.with(holder.image.context)
            .load(R.drawable.ic_icon)
            .into(holder.image)
        val dummy_ett = Cart_Items(data.id.toInt(),"","","","")
        if(CartData(context,dummy_ett,"getid").execute().get() != null){
            holder.add_btn.text = "Remove"
            holder.add_btn.setTextColor(context.resources.getColor(R.color.white))
        }
        else{
            holder.add_btn.setBackgroundResource(R.color.red)
            holder.add_btn.text = "Add"
        }

        if(CartData(context,dummy_ett,"getall").execute().get().toString() != "[]"){
            btn.text = "Proceed To Payment"
            btn.setBackgroundResource(R.color.red) }
        else{btn.setBackgroundResource(R.color.grey)
            btn.text = "No items Selected"
        }


        holder.add_btn.setOnClickListener {
            if (holder.add_btn.text == "Add") {

                Handler().postDelayed(Runnable {
                    try {
                        val ett = Cart_Items(
                            data.id.toInt(),
                            data.name.toString(),
                            data.cost_for_one.toString(),
                            res_name,data.restaurant_id
                        )
                        val ready = CartData(context, ett, "insert").execute().get()
                        if (ready as Boolean) {
                            if(CartData(context,dummy_ett,"getall").execute().get().toString() != "[]"){
                                btn.setText("Proceed To Payment")
                                btn.setBackgroundResource(R.color.red)
                            }else{btn.setBackgroundResource(R.color.grey)
                                btn.setText("No items Selected")}
                            holder.add_btn.setBackgroundResource(R.color.black)
                            holder.add_btn.text = "Remove"
                            holder.add_btn.setTextColor(context.resources.getColor(R.color.white))
                        }


                    }catch (e:Exception){ }
                }, 200)

            } else {
                //add
                Handler().postDelayed(Runnable {
                    try {
                        val ett = Cart_Items(
                            data.id.toInt(),
                            data.name.toString(),
                            data.cost_for_one.toString(),
                            res_name,data.restaurant_id
                        )
                        val ready = CartData(context, ett, "delete").execute().get()
                        if (ready as Boolean) {
                            if(CartData(context,dummy_ett,"getall").execute().get().toString() != "[]"){
                                btn.setText("Proceed To Payment")
                                btn.setBackgroundResource(R.color.red)
                            }else{btn.setBackgroundResource(R.color.grey)
                            btn.setText("No items Selected")}
                            holder.add_btn.setTextColor(context.resources.getColor(R.color.white))
                            holder.add_btn.setBackgroundResource(R.color.red)
                            holder.add_btn.text = "Add"
                        }
                    } finally {
                    }
                }, 200)
            }
        }

    }

}
