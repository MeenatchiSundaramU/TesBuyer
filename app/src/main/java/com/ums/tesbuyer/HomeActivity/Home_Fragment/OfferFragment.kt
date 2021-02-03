package com.ums.tesbuyer.HomeActivity.Home_Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ums.tesbuyer.Authentication.sign_up_class
import com.ums.tesbuyer.R

class OfferFragment : Fragment() {
    companion object {
        var get_data: sign_up_class? = null
        var uids = FirebaseAuth.getInstance().uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_offer, container, false)
        return view
    }
    private fun getData()
    {
        val buyer_ref= FirebaseDatabase.getInstance().getReference("/BuyerAccount/$uids")
        buyer_ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                get_data= p0.getValue(sign_up_class::class.java)!!
            }

        })
    }
    //Getting the data for the Buyer to display profile
    override fun onStart() {
        super.onStart()
        getData()
    }
}