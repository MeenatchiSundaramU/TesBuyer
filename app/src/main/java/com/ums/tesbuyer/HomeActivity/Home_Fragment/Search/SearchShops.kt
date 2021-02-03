package com.ums.tesbuyer.HomeActivity.Home_Fragment.Search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SearchShops(val city:String,val ownername:String,val shopname:String,val uid:String):Parcelable
{
    constructor() : this("","","","")
}