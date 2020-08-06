package com.lamine.realestatemanager.utils

import com.lamine.realestatemanager.models.GeocodeInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *Created by Lamine MESSACI on 06/08/2020.
 */

class RealEstateStream {

    // Stream function
    fun streamFetchGeocodeInfo(address: String, key: String): Observable<GeocodeInfo> =
        RealEstateService.create().getGeocodeInfo(address, key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(10, TimeUnit.SECONDS)
}