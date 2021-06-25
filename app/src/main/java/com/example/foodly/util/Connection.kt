package com.example.foodly.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Connection {
    fun checkConnectivity(context:Context):Boolean{
        val connection = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNet:NetworkInfo? = connection.activeNetworkInfo
        return activeNet?.isConnected != null
}
}