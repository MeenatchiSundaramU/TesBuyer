package com.ums.tesbuyer.HomeActivity

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.ums.tesbuyer.HomeActivity.Home_Fragment.CartFragment
import com.ums.tesbuyer.HomeActivity.Home_Fragment.OfferFragment
import com.ums.tesbuyer.HomeActivity.Home_Fragment.ProfileFragment
import com.ums.tesbuyer.HomeActivity.Home_Fragment.RequirementFragment
import com.ums.tesbuyer.HomeActivity.Home_Fragment.Search.SearchFragment
import com.ums.tesbuyer.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        defaultOpen()
        home_view.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.offer->{
                    defaultOpen()
                }
                R.id.requirement->{
                    supportActionBar!!.title="Requirements"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, RequirementFragment())
                        .commit()
                }
                R.id.search->{
                    supportActionBar!!.title="Search Product"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame,
                            SearchFragment()
                        )
                        .commit()
                }
                R.id.cart->{
                    supportActionBar!!.title="My Cart"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, CartFragment())
                        .commit()
                }
                R.id.profile-> {
                    supportActionBar!!.title = "Profile"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frame, ProfileFragment())
                        .commit()
                }
            }
            return@setOnNavigationItemSelectedListener(true)
        }
    }

    private fun defaultOpen() {
        supportActionBar!!.title="Offer"
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frame, OfferFragment())
            .commit()
    }
    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    override fun onStart() {
        super.onStart()
        if(!isLocationEnabled()) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    }
