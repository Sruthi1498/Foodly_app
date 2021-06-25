package com.example.foodly.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.foodly.R
import com.google.android.material.navigation.NavigationView
import com.example.foodly.database.FavData
import com.example.foodly.database.Fav_res
import com.example.foodly.fragments.FavoriteFragment
import com.example.foodly.fragments.MainFragment
import com.example.foodly.fragments.OrderFragment
import com.example.foodly.fragments.UserFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var toolbar:Toolbar
    lateinit var details: SharedPreferences
    lateinit var navigation_drawer:NavigationView
    lateinit var profile_img:ImageView
    lateinit var frame_layout:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frame_layout =findViewById(R.id.main_frame_layout)
        drawerLayout = findViewById(R.id.main_drawer_layout)
        toolbar = findViewById(R.id.main_toolbar_layout)
        navigation_drawer = findViewById(R.id.main_navigation_view)
        details = getSharedPreferences("details", Context.MODE_PRIVATE)
        profile_img = navigation_drawer.getHeaderView(0).findViewById(R.id.menu_header_img)
        setUpToolBar()
        val drawerAction = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(drawerAction)
        drawerAction.syncState()
        navigation_drawer.getHeaderView(0).findViewById<TextView>(R.id.menu_header_name).text =
            details.getString("name","")
        navigation_drawer.getHeaderView(0).findViewById<TextView>(R.id.menu_header_phone).text =
            details.getString("mobile_number","")
        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout, MainFragment()).commit()
        navigation_drawer.setCheckedItem(R.id.home_select)
        var prev_item = navigation_drawer.checkedItem
        navigation_drawer.setNavigationItemSelectedListener {
            if(prev_item?.itemId != it.itemId){
                    prev_item?.setChecked(false)
            }
            prev_item = it
            when(it.itemId){
                R.id.home_logout -> {
                    val dialog = AlertDialog.Builder(this,R.style.AlertDialogCustom)
                    dialog.setTitle("Log Out").setMessage("Are you sure you want to Log Out?").setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        if(FavData(this, Fav_res("","",""),"deleteall").execute().get() as Boolean) {
                            details.edit().clear().apply()
                            finish()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    }).setNegativeButton("Cancel",DialogInterface.OnClickListener { dialogInterface, i ->  }).
                        setIcon(R.drawable.ic_icon).show()
                    drawerLayout.closeDrawers()
                }
                R.id.home_select ->{
                    supportActionBar?.title = "All Restaurants"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                        MainFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.home_history->{
                    supportActionBar?.title = "Order History"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                        OrderFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.home_fav->{
                    supportActionBar?.title = "Favourite Restaurants"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                        FavoriteFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }
                R.id.home_profile->{
                    supportActionBar?.title = "Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                        UserFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                }

            }
                return@setNavigationItemSelectedListener true
        }
        }

    fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if(supportActionBar?.title == "All Restaurants"){
            finishAffinity()
        }
        else{
            supportActionBar?.title = "All Restaurants"
            supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,
                MainFragment()
            ).commit()
        }
    }



}


