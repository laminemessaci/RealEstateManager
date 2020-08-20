package com.lamine.realestatemanager.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lamine.realestatemanager.models.Picture
import java.lang.reflect.Type
import java.util.*


/**
 *Created by Lamine MESSACI on 03/08/2020.
 */

//This class convert complex data (Picture) for Room database
class Converters {

    private var gson = Gson()
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Picture?>? {
        if(data == null){
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Picture?>?>(){}.type
        return  gson.fromJson(data, listType)
    }
     @TypeConverter
     fun someObjectListToString( someObjects: List<Picture?>?) :String {
         return gson.toJson(someObjects)
     }

}