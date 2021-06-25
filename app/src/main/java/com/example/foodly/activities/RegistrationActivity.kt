package com.example.foodly.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodly.R
import com.example.foodly.util.Connection
import org.json.JSONObject
import java.lang.Exception

class RegistrationActivity : AppCompatActivity() {
    lateinit var bk_button: Button
    lateinit var sign_up_button: Button
    lateinit var sign_up_name: EditText
    lateinit var sign_up_email: EditText
    lateinit var sign_up_mobile: EditText
    lateinit var sign_up_address: EditText
    lateinit var sign_up_password: EditText
    lateinit var sign_up_password_con: EditText
    lateinit var progress_bar:ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        bk_button = findViewById(R.id.signup_back_button)
        sign_up_button = findViewById(R.id.sign_up_button)
        sign_up_name = findViewById(R.id.signup_name)
        sign_up_email = findViewById(R.id.signup_email)
        sign_up_mobile = findViewById(R.id.signup_mobile)
        sign_up_address = findViewById(R.id.signup_address)
        sign_up_password = findViewById(R.id.signup_password)
        sign_up_password_con = findViewById(R.id.signup_password_con)
        progress_bar = findViewById(R.id.sign_up_prgs)
        sign_up_button.setOnClickListener {
            sign_up_button.setBackgroundResource(R.color.tomato_red)
            if (sign_up_name.text.isEmpty() ||
                sign_up_email.text.isEmpty() ||
                sign_up_mobile.text.isEmpty() ||
                sign_up_address.text.isEmpty() ||
                sign_up_password.text.isEmpty() ||
                sign_up_password_con.text.isEmpty()
            ) {
                Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show()

            } else {
                if (sign_up_password.text.toString() != sign_up_password_con.text.toString()) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    if (!sign_up_email.text.toString()
                            .contains('@') || !sign_up_email.text.toString().contains('.')
                    ) {
                        Toast.makeText(this, "Email is incorrect", Toast.LENGTH_SHORT).show()
                    } else {
                        if (Connection().checkConnectivity(this)) {
                            val q = Volley.newRequestQueue(this)
                            val url = "http://13.235.250.119/v2/register/fetch_result"
                            val jsonReg = JSONObject()
                            jsonReg.put("name", sign_up_name.text.toString())
                            jsonReg.put("mobile_number", sign_up_mobile.text.toString())
                            jsonReg.put("password", sign_up_password.text.toString())
                            jsonReg.put("address", sign_up_address.text.toString())
                            jsonReg.put("email", sign_up_email.text.toString())
                            try{
                                progress_bar.visibility = View.VISIBLE
                                val jsonObj = object : JsonObjectRequest(
                                    Method.POST,
                                    url,
                                    jsonReg,
                                    Response.Listener {
                                        val recievedObj = it.getJSONObject("data")
                                        if(recievedObj.getBoolean("success")){
                                            Toast.makeText(this,"Successfully Registered.",Toast.LENGTH_SHORT).show()
                                            progress_bar.visibility = View.GONE
                                            startActivity(Intent(this, MainActivity::class.java))
                                        }
                                        else{
                                            Toast.makeText(this,recievedObj.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                                            progress_bar.visibility = View.GONE
                                        }
                                    },
                                    Response.ErrorListener {
                                        Toast.makeText(this,"Please try again Later...!",Toast.LENGTH_SHORT).show()
                                        progress_bar.visibility = View.GONE
                                    }){
                                    override fun getHeaders(): MutableMap<String, String> {
                                        val headers = HashMap<String, String>()
                                        headers["Content-type"] = "application/json"
                                        headers["token"] = "c3acf1e14c21f9"
                                        return headers
                                    }
                                }
                                q.add(jsonObj)

                            }catch (e:Exception){
                                Toast.makeText(this,"Unknown Error...!",Toast.LENGTH_SHORT).show()
                                progress_bar.visibility = View.GONE
                            }
                        }
                        else{
                            Toast.makeText(this,"Please check your internet Connection.!",Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
            Handler().postDelayed(Runnable {
                sign_up_button.setBackgroundResource(R.color.tomato_red)
            },200)

        }
        bk_button.setOnClickListener {
            Handler().postDelayed(Runnable {
                bk_button.setBackgroundResource(R.drawable.ic_back_arrow)
                startActivity(Intent(this, LoginActivity::class.java))
            }, 300)
        }


    }
    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
    }



}