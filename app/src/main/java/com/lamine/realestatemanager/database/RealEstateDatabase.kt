package com.lamine.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lamine.realestatemanager.database.dao.PropertyDao
import com.lamine.realestatemanager.models.Address
import com.lamine.realestatemanager.models.Picture
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.Converters

/**
 *Created by Lamine MESSACI on 03/08/2020.
 */


// RealEstateManager Database configuration
@Database(entities = [(Property::class), (Picture::class), (Address::class)],version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    companion object {
        private var INSTANCE: RealEstateDatabase? = null

        fun getInstance(context: Context):RealEstateDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,RealEstateDatabase::class.java,"RealEstateManager.db").build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }
    }
}