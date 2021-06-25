package com.example.foodly.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.foodly.R

class UserFragment : Fragment() {
    lateinit var img:ImageView
    lateinit var details:SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        details = activity?.getSharedPreferences("details", Context.MODE_PRIVATE) as SharedPreferences
        val view= inflater.inflate(R.layout.fragment_user, container, false)
        img = view.findViewById(R.id.usr_img)
        var name = view.findViewById<TextView>(R.id.usr_name).setText(details?.getString("name",""))
        var phone = view.findViewById<TextView>(R.id.usr_phone).setText(details?.getString("mobile_number",""))
        var email = view.findViewById<TextView>(R.id.usr_email).setText(details?.getString("email",""))
        var address = view.findViewById<TextView>(R.id.usr_address).setText(details?.getString("address",""))

        return  view
    }


    }