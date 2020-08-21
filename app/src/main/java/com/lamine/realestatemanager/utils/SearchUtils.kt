package com.lamine.realestatemanager.utils

/**
 *Created by Lamine MESSACI on 14/08/2020.
 */
class SearchUtils {

    // Search engine to request properties in database
    fun makeQuery(
        typeOfProperty: String, surfaceMin: Int, surfaceMax: Int,
        roomMin: Int, roomMax: Int, city: String, postalCode: String,
        country: String, shops: Boolean, airport: Boolean,
        park: Boolean, subway: Boolean, school: Boolean,
        trainStation: Boolean, sold: Boolean, available: Boolean,
        priceMin: Double, priceMax: Double, bedRoomsMin: Int,
        bedRoomsMax: Int, entryDate: String, maxDate: String,
        realtorName: String, numberOfBath: Int, numberOfImages: Int
    ): String {
        var query = "SELECT * FROM Property"
        var containsAnd = false

        //Type
        if (typeOfProperty.isNotEmpty() && typeOfProperty != "ALL") {
            query += " WHERE type = '$typeOfProperty'"
            containsAnd = true
        }
        //LivingSpace min
        if (surfaceMin != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "livingSpace >= $surfaceMin"
        }
        //LivingSpace max
        if (surfaceMax != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "livingSpace <= $surfaceMax"
        }
        //Number of min Rooms
        if (roomMin != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "rooms >= $roomMin"
        }
        //Number of max Rooms
        if (roomMax != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "rooms <= $roomMax"
        }
        //City
        if (city != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "city = '$city'"
        }
        //Postal code
        if (postalCode != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "postalCode = '$postalCode'"
        }
        //Country
        if (country != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "country = '$country'"
        }
        //Points of interest
        if ((shops)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "shops = 1"
        }
        if ((airport)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "airport = 1"
        }
        if ((park)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "park = 1"
        }
        if ((subway)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "subway = 1"
        }
        if ((school)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "school = 1"
        }
        if ((trainStation)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "trainStation = 1"
        }
        //Status
        if ((sold)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "status = 0"
        }
        if ((available)) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "status = 1"
        }
        //Price min
        if (priceMin != 0.0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "price >= ${priceMin.toInt()}"
        }
        //Price max
        if (priceMax < 20000000) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "price <= ${priceMax.toInt()}"
        }
        //Number of min Bedrooms
        if (bedRoomsMin != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "numOfBed >= $bedRoomsMin"
        }
        //Number of max Bedrooms
        if (bedRoomsMax != 0) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "numOfBed <= $bedRoomsMax"
        }
        //Entry date
        if (entryDate.isNotEmpty()) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "dateOfEntry >= '$entryDate'"
        }
        //Sold date
        if (maxDate.isNotEmpty()) {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "dateOfEntry <= '$maxDate'"
        }
        //Realtor name
        if (realtorName != "") {
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "realtor = '$realtorName'"
        }
        //Number of Bathrooms
        if (numberOfBath != 0) {
            query += if (containsAnd) " AND " else " WHERE "
            query += "numOfBath = $numberOfBath"
        }
        //Minimum number of Images
        if (numberOfImages != 0) {
            query += if (containsAnd) " AND " else " WHERE "
            query += "(LENGTH(pictures) - LENGTH(REPLACE(pictures, '{', ''))) >= $numberOfImages"
        }
        return query
    }
}