package com.lamine.realestatemanager.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *Created by Lamine MESSACI on 06/08/2020.
 */
class GeocodeInfo {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

}
