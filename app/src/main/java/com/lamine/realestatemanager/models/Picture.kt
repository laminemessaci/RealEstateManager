package com.lamine.realestatemanager.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 *Created by Lamine MESSACI on 30/07/2020.
 */

@Parcelize
@Entity(tableName = "Picture")
data class Picture(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var pictureName: String?,
    var picturePath: String?
) :
    Parcelable {
    constructor() : this(0, "", "")
}