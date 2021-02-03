package com.ums.tesbuyer.HomeActivity.Home_Fragment.Search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.ums.tesbuyer.HomeActivity.Home_Fragment.OfferFragment.Companion.get_data
import com.ums.tesbuyer.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_search__next__shop__show_.*
import kotlinx.android.synthetic.main.search_shops_view.view.*

class Search_Next_Shop_Show_Activity : AppCompatActivity() {
    companion object{
        var ind_buyer_data:FlesherData?=null
    }
    var fleshphoto:String?=null
    var extract_details:ShopClass?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search__next__shop__show_)
        val fleshname=intent.getStringExtra("fleshname")
        fleshphoto=intent.getStringExtra("fleshphoto")
        supportActionBar!!.hide()
        retrieveData(fleshname!!)

    }
    private fun retrieveData(fleshname:String)
    {
        val shops_ref=FirebaseDatabase.getInstance().getReference("/Flesher_Details/$fleshname/${get_data!!.district}/${get_data!!.city}")
        shops_ref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val shop_adapter= GroupAdapter<ViewHolder>()
                for(h in p0.children)
                {
                    val get_shops=h.getValue(SearchShops::class.java)
                    if(get_shops!=null)
                    {
                        shop_adapter.add(ShopClass(get_shops,fleshphoto!!))
                    }
                }
                shop_adapter.setOnItemClickListener { item, view ->
                     extract_details=item as ShopClass
                     val ind= Intent(view.context,ProductActivity::class.java)
                    ind.putExtra("pro_show",extract_details!!.get_s)
                    ind_buyer_data=FlesherData(fleshphoto!!,fleshname,extract_details!!.get_s.city,extract_details!!.get_s.ownername,extract_details!!.get_s.shopname,extract_details!!.get_s.uid)
                    startActivity(ind)
                }
                shop_recycle.adapter=shop_adapter
            }

        })
    }
    class ShopClass(val get_s:SearchShops,val fleshphoto:String):Item<ViewHolder>()
    {
        override fun getLayout(): Int {
            return R.layout.search_shops_view
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            Picasso.get().load(fleshphoto).into(viewHolder.itemView.flesh_image)
              viewHolder.itemView.shopname.text=get_s.shopname
            viewHolder.itemView.ownername.text=get_s.ownername
            viewHolder.itemView.city.text=get_s.city
        }

    }
    class FlesherData(val flesphoto:String,val flesname:String,val city:String,val ownername:String,val shopname:String,val flesh_uid:String)
    {
        constructor() : this("","","","","","")
    }
}