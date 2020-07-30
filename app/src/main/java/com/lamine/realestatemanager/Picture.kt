package com.lamine.realestatemanager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *Created by Lamine MESSACI on 30/07/2020.
 */

@Parcelize
data class Picture(var id:Long,
                   var pictureName:String?,
                   var picturePath:String?) :
    Parcelable {constructor() : this(0, "", "")
}