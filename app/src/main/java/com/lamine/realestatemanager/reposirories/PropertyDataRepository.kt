package com.lamine.realestatemanager.reposirories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.lamine.realestatemanager.database.dao.PropertyDao
import com.lamine.realestatemanager.models.Property

/**
 *Created by Lamine MESSACI on 04/08/2020.
 */
class PropertyDataRepository (private val propertyDao: PropertyDao){

    //--------------GET--------------//
    fun getProperty(id: Long): LiveData<Property> {return  propertyDao.getProperty(id)}

    fun getProperties(): LiveData<List<Property>> { return propertyDao.getAllProperties()}

    fun getPropertyByArgs(query: SimpleSQLiteQuery): LiveData<List<Property>> { return propertyDao.getPropertyByArgs(query)}

    //---------------CREATE------------//
    fun createProperty(property: Property?) { propertyDao.insertProperty(property!!)}

    //---------------UPDATE------------//
    fun updateProperty(property: Property?){propertyDao.updateProperty(property!!)}

    //-------------- DELETE -----------//
    fun deleteProperty(number: Long) {
        propertyDao.deleteProperty(number)
    }
}