package com.ums.tesbuyer.HomeActivity.Home_Fragment.Search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ums.tesbuyer.R
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search, container, false)
        view.chicken.setOnClickListener {
            val ind=Intent(view.context,Search_Next_Shop_Show_Activity::class.java)
            ind.putExtra("fleshname","Chicken")
            ind.putExtra("fleshphoto","https://firebasestorage.googleapis.com/v0/b/tesfood-21d02.appspot.com/o/flesh_photos%2Fchicken.png?alt=media&token=4931df09-ce29-4d79-b91b-a7fe61e08c7e")
            startActivity(ind)
        }
        view.mutton.setOnClickListener {
            val ind=Intent(view.context,Search_Next_Shop_Show_Activity::class.java)
            ind.putExtra("fleshname","Mutton")
            ind.putExtra("fleshphoto","https://firebasestorage.googleapis.com/v0/b/tesfood-21d02.appspot.com/o/flesh_photos%2Fsheep.png?alt=media&token=1ec8bdce-9184-4101-aa6b-5affe8201a6b")
            startActivity(ind)
        }
        view.fish.setOnClickListener {
            val ind=Intent(view.context,Search_Next_Shop_Show_Activity::class.java)
            ind.putExtra("fleshname","Fish")
            ind.putExtra("fleshphoto","https://firebasestorage.googleapis.com/v0/b/tesfood-21d02.appspot.com/o/flesh_photos%2Ffish.png?alt=media&token=8a3f93cf-2157-4ffc-bc57-fe0ef7d055e1")
            startActivity(ind)
        }

        return view
    }


}