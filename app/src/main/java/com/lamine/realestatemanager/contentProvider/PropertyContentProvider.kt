package com.lamine.realestatemanager.contentProvider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.lamine.realestatemanager.database.RealEstateDatabase
import com.lamine.realestatemanager.models.Property

/**
 *Created by Lamine MESSACI on 05/08/2020.
 */
class PropertyContentProvider : ContentProvider() {

    // FOR DATA
    private val authority = "com.lamine.openclassrooms.realestatemanager.provider"
    private val tableName = Property::class.java.simpleName


    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        if (context != null && p1 != null){
            val index = RealEstateDatabase.getInstance(context!!).propertyDao().insertProperty(Property().fromContentValues(p1))
            if (index != 0L){
                context!!.contentResolver.notifyChange(p0,null)
                return ContentUris.withAppendedId(p0,index)
            }
        }
        throw IllegalArgumentException("Failed to insert row into $p0")
    }

    // For request data
    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        if (context != null){
            val index:Long = ContentUris.parseId(p0)
            val cursor = RealEstateDatabase.getInstance(context!!).propertyDao().getPropertyWithCursor(index)
            cursor.setNotificationUri(context!!.contentResolver,p0)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $p0")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw IllegalArgumentException("You can't update row into $p0")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw IllegalArgumentException("You can't delete anything")
    }

    override fun getType(p0: Uri): String? {
        return "vnd.android.cursor.item/$authority.$tableName"
    }

}