package com.example.foodly.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodly.R
import com.example.foodly.adapters.RestaurantAdapter
import com.example.foodly.data.RseList
import com.example.foodly.database.CartData
import com.example.foodly.database.Cart_Items
import com.example.foodly.database.FavData
import com.example.foodly.database.Fav_res
import com.example.foodly.util.Connection
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class DetailsActivity : AppCompatActivity() {
    lateinit var fav_button :FloatingActionButton
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var progress_bar:RelativeLayout
    lateinit var no_net:ImageView
    lateinit var list:ArrayList<RseList>
    lateinit var recl_view:RecyclerView
    lateinit var proceed_btn:Button
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        toolbar = findViewById(R.id.det_toolbar)
        fav_button = findViewById(R.id.det_fav_floating)
        proceed_btn = findViewById(R.id.det_proceed)
        setUpToolbar()
        val ett = Fav_res(intent.getStringExtra("name").toString(),intent.getStringExtra("url").toString(),intent.getStringExtra("id").toString())
        if(FavData(this,ett,"getid").execute().get()!=null){
            fav_button.tooltipText = "Click here to Remove From Favourites"
            fav_button.setImageResource(R.drawable.ic_float_fav_clicked)
        }else{
            fav_button.tooltipText = "Click here to add Favourites"
            fav_button.setImageResource(R.drawable.ic_float_fav)
        }
        fav_button.setOnClickListener {
            Handler().postDelayed(
                Runnable {
                    if(fav_button.tooltipText == "Click here to add Favourites"){
                        if(FavData(this,ett,"insert").execute().get() as Boolean) {
                            fav_button.tooltipText = "Click here to Remove From Favourites"
                            fav_button.setImageResource(R.drawable.ic_float_fav_clicked)}}
                    else{
                        if(FavData(this,ett,"delete").execute().get() as Boolean) {
                            fav_button.setImageResource(R.drawable.ic_float_fav)
                            fav_button.tooltipText = "Click here to add Favourites"
                        }}
                },200)
        }
        progress_bar = findViewById(R.id.det_prgs)
        no_net = findViewById(R.id.det_no_net)
        if(Connection().checkConnectivity(this)) {
            no_net.visibility = View.GONE
            val q = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"+intent.getStringExtra("id")
            try{
                progress_bar.visibility = View.VISIBLE
                val jsonreq = object : JsonObjectRequest(
                    Request.Method.GET,url,null,
                    Response.Listener {
                        if(it.getJSONObject("data").getBoolean("success")){
                            list = arrayListOf<RseList>()
                            val data = it.getJSONObject("data").getJSONArray("data")
                            for(i in 0 until data.length()){
                                list.add(
                                    RseList(
                                        data.getJSONObject(i).getString("id"),
                                        data.getJSONObject(i).getString("name"),
                                        data.getJSONObject(i).getString("cost_for_one"),
                                        data.getJSONObject(i).getString("restaurant_id")
                                    )
                                )
                            }
                            recl_view = findViewById(R.id.det_recl_view)
                            recl_view.layoutManager = LinearLayoutManager(this)
                            recl_view.adapter = RestaurantAdapter(this,list,intent.getStringExtra("name").toString(),proceed_btn)
                            progress_bar.visibility = View.GONE
                        }
                    },
                    Response.ErrorListener {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "c3acf1e14c21f9"
                        return headers
                    }
                }
                q.add(jsonreq)
            }catch (e: Exception){
                progress_bar.visibility = View.GONE
                Toast.makeText(this,"Please Try Again Later.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show()
            no_net.visibility = View.VISIBLE
        }
        proceed_btn.setOnClickListener {
            if(proceed_btn.text == "Proceed To Payment"){
                proceed_btn.setBackgroundResource(R.color.grey)
                Handler().postDelayed(
                    Runnable {
                        proceed_btn.setBackgroundResource(R.color.colorPrimary)
                        startActivity(Intent(this, OrderPlaceActivity::class.java))
                    },200)
            }
        }
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = intent.getStringExtra("name")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            val dummy_ett = Cart_Items(0,"","","","")
            CartData(this,dummy_ett,"deleteall").execute().get()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        CartData(this,Cart_Items(0,"","","",""),"deleteall").execute().get()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}