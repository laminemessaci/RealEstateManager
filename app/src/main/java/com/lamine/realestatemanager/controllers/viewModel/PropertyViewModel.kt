package com.lamine.realestatemanager.controllers.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.repositories.PropertyDataRepository
import java.util.concurrent.Executor

/**
 *Created by Lamine MESSACI on 05/08/2020.
 */
class PropertyViewModel(
    private val mPropertyDataRepository: PropertyDataRepository,
    private val executor: Executor
) : ViewModel() {
    ///// --- PROPERTY --- /////

    // --- GET ---
    fun getAllProperty(): LiveData<List<Property>> {
        return mPropertyDataRepository.getProperties()
    }

    fun getProperty(propertyId: Long): LiveData<Property> {
        return mPropertyDataRepository.getProperty(propertyId)
    }

    // --- CREATE ---
    fun createProperty(property: Property?) {
        executor.execute { mPropertyDataRepository.createProperty(property) }
    }

    // --- UPDATE ---
    fun updateProperty(property: Property?) {
        executor.execute { mPropertyDataRepository.updateProperty(property) }
    }

    // --- DELETE ---
    fun deleteProperty(propertyId: Long) {
        mPropertyDataRepository.deleteProperty(propertyId)
    }

    //// --- SEARCH --- ////
    fun getPropertyByArgs(queryString: String): LiveData<List<Property>> {
        val query = SimpleSQLiteQuery(queryString)
        Log.e("get properties by args", "Query : ${query.sql}")
        return mPropertyDataRepository.getPropertyByArgs(query)
    }
}