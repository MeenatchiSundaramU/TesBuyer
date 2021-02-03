package com.ums.tesbuyer.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ums.tesbuyer.HomeActivity.HomeActivity
import com.ums.tesbuyer.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.jar.Manifest

class SignUpActivity : AppCompatActivity() {
    lateinit var full_name:String
    lateinit var phone:String
    lateinit var  email:String
    lateinit var pass:String
    lateinit var conf_pass:String
    lateinit var district:String
    lateinit var city:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar!!.title="TesBuyer's Registration"
        sign_in.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }
        createuser.setOnClickListener {
            createUser()
        }
    }
    private fun createUser()
    {
        full_name=signup_fullname.text.toString()
        phone=signup_phone.text.toString()
        email=signup_email.text.toString()
        pass=signup_pass.text.toString()
        conf_pass=signup_conf_pass.text.toString()
        district=signup_district.text.toString()
        city=signup_city.text.toString()
        when{
            TextUtils.isEmpty(full_name)->{
                signup_fullname.error="Full Name is Required"
                signup_fullname.requestFocus()
            }
            TextUtils.isEmpty(phone)->{
                signup_phone.error="Phone Number is Required"
                signup_phone.requestFocus()
            }
            TextUtils.isEmpty(email)->{
                signup_email.error="Email is Required"
                signup_email.requestFocus()
            }
            TextUtils.isEmpty(pass)->{
                signup_pass.error="Password is Required"
                signup_pass.requestFocus()
            }
            TextUtils.isEmpty(conf_pass)->{
                signup_conf_pass.error="Confirm Password is Required"
                signup_conf_pass.requestFocus()
            }
            TextUtils.isEmpty(district)->{
                signup_district.error="District Name is Required"
                signup_district.requestFocus()
            }
            TextUtils.isEmpty(city)->{
                signup_city.error="City Name is Required"
                signup_city.requestFocus()
            }
            else->{
                sign_up_progress.visibility=View.VISIBLE
                val signup_auth= FirebaseAuth.getInstance()
                signup_auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        signup_upload(it.result!!.user.uid)
                    }
                    else
                    {
                        sign_up_progress.visibility=View.INVISIBLE
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        signup_auth.signOut()
                    }
                }
            }
        }
    }

    private fun signup_upload(uid: String)
    {
        val signup_ref=FirebaseDatabase.getInstance().getReference("/BuyerAccount/$uid")
        signup_ref.setValue(sign_up_class(full_name,phone,email,pass, conf_pass, district, city)).addOnCompleteListener {
            if(it.isSuccessful)
            {
                sign_up_progress.visibility=View.INVISIBLE
               Toast.makeText(this,"Data Saved Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,HomeActivity::class.java))
                finish()
            }
            else
            {
                sign_up_progress.visibility=View.INVISIBLE
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
            }
        }

    }
}