package com.lamine.realestatemanager.controllers.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.Address
import com.lamine.realestatemanager.models.Picture
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.Utils
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_create_estate.*
import org.jetbrains.annotations.NonNls
import java.util.*

class CreateEstateActivity : AppCompatActivity(){

    val listOfTypes = arrayOf("Manor", "House", "Castle", "Flat", "Loft", "Apartment", "Duplex")
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

    // To check internet and location
    private fun checkDeviceServices() {
        if (!Utils.isInternetAvailable(this)) {
            showAlertDialogDevice("internet")
        }
        if (!Utils.isLocationEnabled(this)) {
            showAlertDialogDevice("location")
        }
    }

    // To display AlertDialogs
    private fun showAlertDialogDevice(s: String) {
        var title = ""
        var text = ""
        var intent = Intent()
        lateinit var mAlertDialog: AlertDialog
        when (s) {
            "location" -> {
                title = resources.getString(R.string.gps_title)
                text = resources.getString(R.string.gps_text)
                intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            }
            "internet" -> {
                title = resources.getString(R.string.internet_title)
                text = resources.getString(R.string.internet_text)
                intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            }
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(text)
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.no) { _, _ ->
            mAlertDialog.dismiss()
        }
        mAlertDialog = builder.show()
    }

    // init ViewModel
    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(
            this,
            DataInjection.Injection.provideViewModelFactory(this)
        ).get(PropertyViewModel::class.java)
    }

    //To request intent data
    private fun getTheBundle() {
        propertyId = intent.getLongExtra(DetailActivity.PROPERTY, 0)
        if (propertyId != 0L) {
            getSelectProperty()
            isEdit = true
        } else {
            property = Property()
            checkbox_available.isChecked = true
        }
    }
    // Request selected property
    private fun getSelectProperty() {
        propertyViewModel.getProperty(propertyId).observe(this, androidx.lifecycle.Observer { property ->
            property?.let { initVars(it) }
        })
    }


    // Variables initialisation to edit property
    private fun initVars(property: Property) {
        this.property = property
        typeOfProperty = property.type
        if (property.livingSpace != null) surface = property.livingSpace!!
        if (property.rooms != null) numberOfRooms = property.rooms!!
        if (property.numOfBed != null) numberOfBed = property.numOfBed!!
        if (property.numOfBath != null) numberOfBath = property.numOfBath!!
        if (property.description != null) description = property.description!!
        if (property.price != null) price = property.price!!
        if (property.realtor != null) realtorName = property.realtor!!
        if (property.dateOfEntry != null) entryDate = property.dateOfEntry!!
        if (property.dateOfSale != null) soldDate = property.dateOfSale!!
        if (property.status != null) sold = property.status!!
        if (property.airport != null) this.airport = property.airport!!
        if (property.subway != null) this.subway = property.subway!!
        if (property.school != null) this.school = property.school!!
        if (property.shops != null) this.shops = property.shops!!
        if (property.trainStation != null) this.trainStation = property.trainStation!!
        if (property.park != null) this.park = property.park!!
        if (property.pictures != null) this.pictures = property.pictures as ArrayList<Picture>
        if (property.address!!.city != null) city = property.address!!.city.toString()
        if (property.address!!.postalCode != null) postalCode =
            property.address!!.postalCode.toString()
        if (property.address!!.country != null) country = property.address!!.country.toString()
        if (property.address!!.additionalAddress != null) additionalAddress =
            property.address!!.additionalAddress.toString()
        if (property.address!!.lat != null) lat = property.address!!.lat!!
        if (property.address!!.lng != null) lng = property.address!!.lng!!
        if (property.address?.apartmentNumber != 0) {
            this.apartNumber = property.address?.apartmentNumber!!
            apart_number.isVisible = true
            edit_apart_nbr.isVisible = true
            edit_apart_nbr.setText(apartNumber.toString())
        }
        initWidgets()
    }
    // Views initialisation
    private fun initWidgets() {

        val spinnerPosition: Int = listOfTypes.indexOf(typeOfProperty)
        type_spinner.setSelection(spinnerPosition)
        if (property.livingSpace != null) edit_surface.setText(surface.toString())
        if (numberOfRooms != 0) edit_nbr_rooms.setText(numberOfRooms.toString())
        if (numberOfBed != 0) edit_nbr_bed.setText(numberOfBed.toString())
        if (numberOfBath != 0) edit_nbr_bath.setText(numberOfBath.toString())
        if (property.address != null) {
            edit_address.setText(property.address!!.address)
        } else {
            address = Address()
        }
        if (property.realtor != null) edit_realtor.setText(realtorName)
        if (property.description != null) edit_description.setText(description)
        if (property.price != null) picker_price.setText(price.toString())
        if (property.dateOfSale != null) picker_sold_date.text = soldDate
        if (property.dateOfEntry != null) picker_entry_date.text = entryDate
        initCheckbox()
        if (property.status != null && property.status == true) {
            checkbox_available.isChecked = true
        } else {
            checkbox_sold.isChecked = true
        }
        if (city.isNotEmpty()) edit_city.setText(city)
        if (postalCode.isNotEmpty()) edit_postal_code.setText(postalCode)
        if (country.isNotEmpty()) edit_country.setText(country)
        if (additionalAddress.isNotEmpty()) edit_additional_address.setText(additionalAddress)

    }

    // To init checkbox
    private fun initCheckbox() {
        if (airport) {
            checkbox_airport.isChecked = true
        }
        if (school) {
            checkbox_school.isChecked = true
        }
        if (shops) {
            checkbox_shops.isChecked = true
        }
        if (subway) {
            checkbox_subway.isChecked = true
        }
        if (trainStation) {
            checkbox_train_station.isChecked = true
        }
        if (park) {
            checkbox_park.isChecked = true
        }

    }


}