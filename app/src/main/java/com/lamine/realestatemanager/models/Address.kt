package com.lamine.realestatemanager.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *Created by Lamine MESSACI on 30/07/2020.
 */
@Parcelize
data class Address(var addressId:Long,
                   var address:String?,
                   var sector:String?,
                   var apartmentNumber:Int?,
                   var city:String?,
                   var postalCode:String?,
                   var country:String?,
                   var lat:Double?,
                   var lng:Double?,
                   var additionalAddress:String?) :
    Parcelable {
    constructor() : this(0, "", "",0,"","","",0.0,0.0,"")

}
