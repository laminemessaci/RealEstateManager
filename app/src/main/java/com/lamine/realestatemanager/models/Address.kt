package com.lamine.realestatemanager.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 *Created by Lamine MESSACI on 30/07/2020.
 */
@Parcelize
@Entity(tableName = "Address")
data class Address(@PrimaryKey @ColumnInfo(name = "addressId")var addressId:Long,
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
