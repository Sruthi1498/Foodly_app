package com.example.foodly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.foodly.activities.LoginActivity
import com.example.foodly.activities.MainActivity
import kotlinx.android.synthetic.main.splash_activity.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        var details: SharedPreferences = getSharedPreferences("details", Context.MODE_PRIVATE)
        if(details.getBoolean("loggined",false)){
            Handler().postDelayed(Runnable { startActivity(Intent(this,
                MainActivity::class.java)) },500)
        }
        else {
            get_started.setOnClickListener {
                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )

            }
        }

    }
}
