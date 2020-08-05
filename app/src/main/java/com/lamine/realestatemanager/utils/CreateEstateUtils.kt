package com.lamine.realestatemanager.utils

import android.content.Context
import android.widget.Toast
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.models.Address
import com.lamine.realestatemanager.models.Picture
import com.lamine.realestatemanager.models.Property

/**
 *Created by Lamine MESSACI on 05/08/2020.
 */
class CreateEstateUtils {

    // To check values before storage
    fun checkValueBeforeStoreProperty(
        context: Context,
        typeOfProperty: String,
        surface: Int,
        numberOfRooms: Int,
        numberOfBed: Int,
        numberOfBath: Int,
        address: Address,
        price: Double,
        realtorName: String,
        entryDate: String,
        soldDate: String,
        sold: Boolean,
        property: Property,
        city: String,
        postalCode: String,
        country: String
    ): Property {
        val propertyToReturn = Property(
            0, "", "", 0.0, 0, 0,
            shops = false,
            trainStation = false,
            park = false,
            airport = false,
            subway = false,
            school = false,
            status = true,
            dateOfEntry = "",
            dateOfSale = "",
            realtor = "",
            numOfBath = 0,
            numOfBed = 0,
            address = null,
            pictures = emptyList()
        )

        if (typeOfProperty.isNotEmpty()) {
            property.type = typeOfProperty
            property.address = Address()
            if (price != 0.0) {
                property.price = price
                if (numberOfRooms != 0) {
                    property.rooms = numberOfRooms
                    if (numberOfBed != 0) {
                        property.numOfBed = numberOfBed
                        if (numberOfBath != 0) {
                            property.numOfBath = numberOfBath
                            if (surface != 0) {
                                property.livingSpace = surface
                                if (realtorName.isNotEmpty()) {
                                    property.realtor = realtorName
                                    if (address.address?.isNotEmpty()!!) {
                                        property.address = address
                                        if (city.isNotEmpty()) {
                                            property.address!!.city = city
                                            if (postalCode.isNotEmpty()) {
                                                property.address!!.postalCode = postalCode
                                                if (country.isNotEmpty()) {
                                                    property.address!!.country = country
                                                    if (sold) {
                                                        if (entryDate != "") {
                                                            property.dateOfEntry = entryDate
                                                            return property
                                                        } else showToast(
                                                            context,
                                                            context.resources.getString(R.string.entry_date_text)
                                                        )
                                                    } else if (!sold) {
                                                        if (soldDate != "") {
                                                            property.dateOfSale = soldDate
                                                            return property
                                                        } else showToast(
                                                            context,
                                                            context.resources.getString(R.string.sold_date_text)
                                                        )
                                                    }
                                                } else showToast(
                                                    context,
                                                    context.resources.getString(R.string.country_text)
                                                )

                                            } else showToast(
                                                context,
                                                context.resources.getString(R.string.postal_text)
                                            )

                                        } else showToast(
                                            context,
                                            context.resources.getString(R.string.city_text)
                                        )

                                    } else showToast(
                                        context,
                                        context.resources.getString(R.string.address_text)
                                    )

                                } else showToast(
                                    context,
                                    context.resources.getString(R.string.realtor_text)
                                )

                            } else showToast(
                                context,
                                context.resources.getString(R.string.space_text)
                            )

                        } else showToast(context, context.resources.getString(R.string.bath_text))

                    } else showToast(context, context.resources.getString(R.string.numBed_text))

                } else showToast(context, context.resources.getString(R.string.room_text))

            } else showToast(context, context.resources.getString(R.string.price_text))

        } else showToast(context, context.resources.getString(R.string.type_text))

        return propertyToReturn
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    // Set values in property
    fun setValuesInProperty(
        lat: Double,
        lng: Double,
        airport: Boolean,
        school: Boolean,
        subway: Boolean,
        shops: Boolean,
        trainStation: Boolean,
        park: Boolean,
        additionalAddress: String,
        pictures: List<Picture>,
        address: Address,
        description: String,
        entryDate: String,
        apartNumber: Int,
        sold: Boolean,
        soldDate: String,
        property: Property
    ): Property {

        if (additionalAddress.isNotEmpty()) property.address!!.additionalAddress =
            additionalAddress
        if (description.isNotEmpty()) property.description = description
        if (pictures.isNotEmpty()) property.pictures = pictures
        property.dateOfSale = soldDate
        address.apartmentNumber = apartNumber
        property.airport = airport
        property.park = park
        property.school = school
        property.subway = subway
        property.shops = shops
        property.trainStation = trainStation
        property.status = sold
        property.dateOfEntry = entryDate
        property.dateOfSale = soldDate
        if (lat != 0.0) property.address!!.lat = lat
        if (lng != 0.0) property.address!!.lng = lng
        return property
    }

    // To make address
    fun checksAddressElements(address: Address, city: String, postalCode: String): String {
        var addressStr = ""
        if (address.address?.isNotEmpty()!! && city.isNotEmpty() && postalCode.isNotEmpty()) {
            addressStr = address.address + "+" + city + postalCode
        }
        return addressStr
    }
}