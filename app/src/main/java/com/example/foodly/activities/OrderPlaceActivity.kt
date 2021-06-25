package com.example.foodly.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodly.R
import com.google.android.material.appbar.AppBarLayout
import com.example.foodly.adapters.OrderAdapter

class OrderPlaceActivity : AppCompatActivity() {
    lateinit var recl_view: RecyclerView
    lateinit var place_order_button:Button
    lateinit var ord_res_name:TextView
    lateinit var prgs:RelativeLayout
    lateinit var order_placed:ConstraintLayout
    lateinit var appbar:AppBarLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        val details = getSharedPreferences("details", Context.MODE_PRIVATE)
        recl_view = findViewById(R.id.ord_recl_view)
        toolbar = findViewById(R.id.ord_toolbar)
        ord_res_name = findViewById(R.id.ord_res_name)
        prgs = findViewById(R.id.ord_prgs)
        appbar = findViewById(R.id.ord_appBarLayout)
        order_placed = findViewById(R.id.ord_placed)
        place_order_button = findViewById(R.id.ord_place_order)
        setUpToolbar()
        recl_view.layoutManager = LinearLayoutManager(this)
        recl_view.adapter = OrderAdapter(this,place_order_button,ord_res_name,prgs,order_placed,appbar,details.getString("user_id","") as String)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            super.onBackPressed()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
    }



}

