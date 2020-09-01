package com.lamine.realestatemanager.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *Created by Lamine MESSACI on 06/08/2020.
 */
class Location {

    @SerializedName("lat")
    @Expose
    var lat: Double = 0.0

    @SerializedName("lng")
    @Expose
    var lng: Double = 0.0
}