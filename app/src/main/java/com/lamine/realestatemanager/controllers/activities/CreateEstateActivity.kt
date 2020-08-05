package com.lamine.realestatemanager.controllers.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.Address
import com.lamine.realestatemanager.models.Picture
import com.lamine.realestatemanager.models.Property
import io.reactivex.disposables.Disposable
import java.util.*

class CreateEstateActivity : AppCompatActivity() {
    // 1 - FOR DATA
    private var isEdit: Boolean = false
    private var propertyId: Long = 0
    private lateinit var disposable: Disposable

    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var typeOfProperty: String
    private var surface: Int = 0
    private var numberOfRooms: Int = 0
    private var numberOfBed: Int = 0
    private var numberOfBath: Int = 0
    private var apartNumber: Int = 0
    private var address: Address = Address()
    private var description: String = ""
    private var price: Double = 0.0
    private var realtorName: String = ""
    private var entryDate: String = ""
    private var soldDate: String = ""
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private var sold: Boolean = true
    private var pictures = arrayListOf<Picture>()
    private lateinit var property: Property
    private var airport: Boolean = false
    private var school: Boolean = false
    private var subway: Boolean = false
    private var shops: Boolean = false
    private var trainStation: Boolean = false
    private var park: Boolean = false
    private var imageUri: Uri? = null
    private var city: String = ""
    private var postalCode: String = ""
    private var country: String = ""
    private var additionalAddress: String = ""
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private lateinit var alertDialog: AlertDialog
  

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)

    }
}