package com.lamine.realestatemanager.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *Created by Lamine MESSACI on 06/08/2020.
 */
class Result {
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null
}