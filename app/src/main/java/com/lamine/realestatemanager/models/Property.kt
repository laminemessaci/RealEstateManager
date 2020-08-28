package com.lamine.realestatemanager.models

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

/**
 *Created by Lamine MESSACI on 30/07/2020.
 */
@Parcelize
@Entity
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class Property(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var type: String,
    var description: String?,
    var price: Double?,
    var livingSpace: Int?,
    var rooms: Int? =0,
    var shops: Boolean?,
    var trainStation: Boolean?,
    var park: Boolean?,
    var airport: Boolean?,
    var subway: Boolean?,
    var school: Boolean?,
    var status: Boolean?,
    var dateOfEntry: String?,
    var dateOfSale: String?,
    var realtor: String?,
    var numOfBath: Int?,
    var numOfBed: Int?,
    @Embedded
    var address: Address?,

    @TypeConverters
    var pictures: List<Picture>? = arrayListOf()
):Parcelable
{
    constructor() : this(
        0, "", "", 0.0, 0,
        0, false,false,false,false,false,false, true, "", "",
        "",0, 0, null, emptyList()
    )
    fun fromContentValues(values: ContentValues): Property {
        val property = Property()
        if (values.containsKey("id")) property.id= values.getAsLong("id")
        if (values.containsKey("type")) property.type = values.getAsString("type")
        if (values.containsKey("description")) property.description = values.getAsString("description")
        if (values.containsKey("price")) property.price = values.getAsDouble("price")
        if (values.containsKey("livingSpace")) property.livingSpace = values.getAsInteger("livingSpace")
        if (values.containsKey("rooms")) property.rooms = values.getAsInteger("rooms")
        if (values.containsKey("shops")) property.shops = values.getAsBoolean("shops")
        if (values.containsKey("trainStation")) property.trainStation = values.getAsBoolean("trainStation")
        if (values.containsKey("park")) property.park = values.getAsBoolean("park")
        if (values.containsKey("airport")) property.airport = values.getAsBoolean("airport")
        if (values.containsKey("subway")) property.subway = values.getAsBoolean("subway")
        if (values.containsKey("school")) property.school = values.getAsBoolean("school")
        if (values.containsKey("status")) property.status = values.getAsBoolean("status")
        if (values.containsKey("dateOfEntry")) property.dateOfEntry = values.getAsString("dateOfEntry")
        if (values.containsKey("dateOfSale")) if (values.getAsString("dateOfSale") == "") property.dateOfSale = null else property.dateOfSale = values.getAsString("dateOfSale")
        if (values.containsKey("realtor")) property.realtor = values.getAsString("realtor")
        if (values.containsKey("numOfBath")) property.numOfBath = values.getAsInteger("numOfBath")
        if (values.containsKey("numOfBed")) property.numOfBed = values.getAsInteger("numOfBed")
        if (values.containsKey("address")) property.address?.address = values.getAsString("address")
        if (values.containsKey("apartmentNumber")) if (values.getAsInteger("apartmentNumber") == 0) property.address?.apartmentNumber = null else property.address?.apartmentNumber = values.getAsInteger("apartmentNumber")
        if (values.containsKey("city")) property.address?.city = values.getAsString("city")
        if (values.containsKey("postalCode")) property.address?.postalCode = values.getAsString("postalCode")
        if (values.containsKey("country")) property.address?.country = values.getAsString("country")
        if (values.containsKey("additionalAddress")) property.address?.additionalAddress = values.getAsString("additionalAddress")
        if (values.containsKey("lat")) if (values.getAsDouble("lat") == 0.0) property.address?.lat = null else property.address?.lat = values.getAsDouble("lat")
        if (values.containsKey("lng")) if (values.getAsDouble("lng") == 0.0) property.address?.lng = null else property.address?.lng = values.getAsDouble("lng")
        return property
    }
}
