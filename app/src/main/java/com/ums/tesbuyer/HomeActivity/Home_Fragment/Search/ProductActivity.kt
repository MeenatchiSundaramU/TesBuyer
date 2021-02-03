package com.ums.tesbuyer.HomeActivity.Home_Fragment.Search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.*
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.ums.tesbuyer.HomeActivity.Home_Fragment.OfferFragment.Companion.get_data
import com.ums.tesbuyer.HomeActivity.Home_Fragment.OfferFragment.Companion.uids
import com.ums.tesbuyer.HomeActivity.Home_Fragment.Search.Search_Next_Shop_Show_Activity.Companion.ind_buyer_data
import com.ums.tesbuyer.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.product_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.RandomAccess
import kotlin.coroutines.coroutineContext
import androidx.core.app.ActivityCompat.checkSelfPermission as checkSelfPermission1

class ProductActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val get_data=intent.getParcelableExtra<SearchShops>("pro_show")
        supportActionBar!!.title=ind_buyer_data!!.shopname+" "+ind_buyer_data!!.flesname+"'s"+" "+"Shops"
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getData(fusedLocationProviderClient,this)

    }
    @SuppressLint("SimpleDateFormat")
    private fun getData(fuseloc:FusedLocationProviderClient,views:Context)
    {
        val calendar=Calendar.getInstance()
        val date=SimpleDateFormat("dd-MM-y").format(calendar.time).toString()
        val flesher_ref=FirebaseDatabase.getInstance().getReference("/FlesherAddProduct/${ind_buyer_data!!.flesname}/${get_data!!.district}/${get_data!!.city}/$date/${ind_buyer_data!!.flesh_uid}")
        Log.d("GETTING","${ind_buyer_data!!.flesname}/${get_data!!.district}/${get_data!!.city}/$date/${ind_buyer_data!!.flesh_uid}")
        flesher_ref.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val pro_adapter = GroupAdapter<ViewHolder>()
                for (h in p0.children) {
                    val get_product = h.getValue(Poduct::class.java)
                    pro_adapter.add(ProAdapter(get_product!!,fuseloc,views))
                }
                pro_recycle.adapter=pro_adapter
            }
        })
    }
    }


    class ProAdapter(val product:Poduct, var locationss:FusedLocationProviderClient, val views:Context): Item<ViewHolder>()
    {
        lateinit var locationRequest: LocationRequest
        val PERMISSION_ID = 1010
        var tot_price:Double?=null
        var latitude:String?=null
        var longitude:String?=null
        override fun getLayout(): Int {
            return R.layout.product_view
        }

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        override fun bind(viewHolder: ViewHolder, position: Int) {
            Picasso.get().load(ind_buyer_data!!.flesphoto).into(viewHolder.itemView.flesh_image_show)
            viewHolder.itemView.fleshname.text = product.fleshname
            viewHolder.itemView.varivalue.text = product.fleshvarities
            viewHolder.itemView.totalvalue.text = product.totalflesh
            viewHolder.itemView.pricevalue.text = product.fleshprice
            viewHolder.itemView.growvalue.text = product.fleshlat








            viewHolder.itemView.getpricebtn.setOnClickListener {
                tot_price=(viewHolder.itemView.req_value.text.toString().toDouble()*viewHolder.itemView.pricevalue.text.toString().toDouble())
                viewHolder.itemView.price_display.setText(viewHolder.itemView.price_display.text.toString()+tot_price.toString())

            }
            viewHolder.itemView.cartbtn.setOnClickListener {
                getLastLocation()
                if(latitude==null)
                {
                    Log.d("GETSLOC","NO LOCATIONS")
                    return@setOnClickListener
                }
                val quan=viewHolder.itemView.req_value.text.toString()
                val otp_flesh=((Math.random()*9000).toInt()+1000).toString()
                val otp_buyer=((Math.random()*9000).toInt()+1000).toString()
                val orderid=UUID.randomUUID().toString()
                val calendar=Calendar.getInstance()
                val date=SimpleDateFormat("dd-MM-y").format(calendar.time).toString()
                val flesh_ref=FirebaseDatabase.getInstance().getReference("/FleshOrder/${get_data!!.district}/${get_data!!.city}/$date/${ind_buyer_data!!.flesh_uid}").push()
                val buyerref=FirebaseDatabase.getInstance().getReference("/BuyerOrder/$uids/$date").push()
                buyerref.setValue(BuyerNotifi(product.fleshname,product.fleshvarities,quan,otp_buyer,tot_price.toString(),orderid))
                val delivery_ref=FirebaseDatabase.getInstance().getReference("/DeliveryOrder/${get_data!!.district}/${get_data!!.city}/$date").push()
                delivery_ref.setValue(Delivery(product.fleshname,product.fleshvarities,quan,tot_price.toString(),
                    ind_buyer_data!!.ownername,
                    ind_buyer_data!!.shopname,
                    get_data!!.full_name,
                    get_data!!.phone,orderid,latitude!!,longitude!!,product.fleshphone,product.fleshlat,product.fleshlong,otp_flesh,otp_buyer))
                flesh_ref.setValue(FlesherNotifi(product.fleshname,product.fleshvarities,quan,otp_flesh,orderid)).addOnSuccessListener {
                    viewHolder.itemView.req_value.text.clear()
                    viewHolder.itemView.price_display.text.clear()
                }
            }
        }

        @SuppressLint("MissingPermission", "SetTextI18n")
        fun getLastLocation() {
            locationss.lastLocation.addOnCompleteListener { task ->
                var location: Location? = task.result
                if (location == null) {
                    NewLocationData()
                } else {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()

                }
            }
        }



        @SuppressLint("MissingPermission")
        fun NewLocationData(){
            var locationRequest =  LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 0
            locationRequest.fastestInterval = 0
            locationRequest.numUpdates = 1
            locationss = LocationServices.getFusedLocationProviderClient(views)
            locationss!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
            )
        }


        private val locationCallback = object : LocationCallback(){
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(locationResult: LocationResult) {
                var lastLocation: Location = locationResult.lastLocation
                Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
                latitude=lastLocation.latitude.toString()
                longitude=lastLocation.longitude.toString()
            }
        }
    }
class FlesherNotifi(val fleshtype:String,val fleshvari:String,val totalquan:String,val otp:String,val orderid:String)//flesher+driver
{
    constructor() : this("","","","","")
}
class BuyerNotifi(val fleshtype:String,val fleshvari:String,val totalquan:String,val otp:String,val totalprice:String,val orderid:String)
{
    constructor() : this("","","","","","")
}
class Delivery(val fleshtype:String,val fleshvari:String,val totalquan:String,val totalprice:String,val fleshername:String,val fshopname:String,val buyername:String,val buyermobileno:String,val orderid:String,val buyerlat:String,val buyerlong:String,val flesherphone:String,val flesherlat:String,val flesherlong: String,val flesherotp:String,val buyerotp:String)
{
    constructor() : this("","","","","","","","","","","","","","","","")
}