package com.lamine.realestatemanager.controllers.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.android.gms.maps.model.LatLng
import com.lamine.realestatemanager.BuildConfig
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.*
import com.lamine.realestatemanager.utils.CreateEstateUtils
import com.lamine.realestatemanager.utils.NotificationClass
import com.lamine.realestatemanager.utils.RealEstateStream
import com.lamine.realestatemanager.utils.Utils
import com.lamine.realestatemanager.view.DetailPictureAdapter
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_create_estate.*
import kotlinx.android.synthetic.main.picture_title_dialogue.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class CreateEstateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val listOfTypes = arrayOf("Manor", "House", "Castle", "Flat", "Loft", "Apartment", "Duplex")

    // 1 - FOR DATA
    private var isEdit: Boolean = false
    private var propertyId: Long = 0
    private lateinit var disposable: Disposable
    private lateinit var geoLocation: GeocodeInfo
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
    private val checkClass = CreateEstateUtils()
    private val location: Location = Location()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)
        checkDeviceServices()
        initViewModelFactory()
        getTheBundle()
        configureToolbar()
        configureSpinner()
        configureSurface()
        configureNumRooms()
        configureNumBed()
        configureNumBath()
        configureNumApart()
        configureAddress()
        configureCity()
        configurePostalCode()
        configureCountry()
        configureEditAddress()
        configureDescription()
        configurePrice()
        configureRealtorName()
        configureDatePickerEntry()
        configureDatePickerSold()
        configureCheckBoxClick()
        configureButtons()
        configureButtonValidate()
    }

    // Toolbar configuration
    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Edit Property"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.custom_indicator)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //toolbar.setNavigationOnClickListener{
         //   onBackPressed()
       // }
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
        propertyId = intent.getLongExtra("property", 0)
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
        propertyViewModel.getProperty(propertyId)
            .observe(this, androidx.lifecycle.Observer { property ->
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
        configureRecyclerView()
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
        configureRecyclerView()
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

    // Buttons configuration
    private fun configureButtons() {
        button_gallery.setOnClickListener {
            checkPermissionToReadStorage()
        }
        button_take_picture.setOnClickListener {
            checkPermissionToAccessCamera()
        }
    }

    // RecyclerView config
    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        create_picture_recycler_view.apply {
            layoutManager =
                LinearLayoutManager(applicationContext, OrientationHelper.HORIZONTAL, false)
            adapter = DetailPictureAdapter(pictures) { position: Int -> onItemClicked(position) }
        }
    }

    // Date picker to sold date
    private fun configureDatePickerSold() {
        picker_sold_date.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    picker_sold_date.text = Utils.getStringDate(year, dayOfMonth, monthOfYear)
                    soldDate = picker_sold_date.text.toString()
                    sold = false
                    checkbox_available.isChecked = false
                    checkbox_sold.isChecked = true
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        picker_sold_date.setOnLongClickListener {
            soldDate = ""
            picker_sold_date.text = soldDate
            true
        }
    }

    //// Date picker to entry date
    private fun configureDatePickerEntry() {
        entryDate = Utils.getStringDate(year, month, day)
        picker_entry_date.text = Utils.getTodayDate()
        picker_entry_date.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    picker_entry_date.text = Utils.getStringDate(year, dayOfMonth, monthOfYear)
                    entryDate = picker_entry_date.text.toString()
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        picker_entry_date.setOnLongClickListener {
            entryDate = ""
            picker_entry_date.text = entryDate
            true
        }

    }


    // Realtor name configuration
    private fun configureRealtorName() {
        edit_realtor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                realtorName = edit_realtor.text.toString().replace(" ", "")
            }
        })
    }

    // Price config
    private fun configurePrice() {
        picker_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val priceStr: String = picker_price.text.toString()
                if (priceStr.isNotEmpty()) price = priceStr.toDouble()
            }
        })
    }

    // Description config
    private fun configureDescription() {
        edit_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                description = edit_description.text.toString()
            }
        })
    }

    // Address config
    private fun configureAddress() {
        edit_address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                address = Address()
                address.address = edit_address.text.toString()
            }
        })
    }

    // City config
    private fun configureCity() {
        edit_city.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                city = edit_city.text.toString().replace(" ", "")
            }
        })
    }

    // Postal Code config
    private fun configurePostalCode() {
        edit_postal_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                postalCode = edit_postal_code.text.toString().replace(" ", "")
            }
        })
    }


    // Country config
    private fun configureCountry() {
        edit_country.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                country = edit_country.text.toString().replace(" ", "")
            }
        })
    }

    // Additional address config
    private fun configureEditAddress() {
        edit_additional_address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                additionalAddress = edit_additional_address.text.toString()
            }
        })
    }

    // Number of rooms config
    private fun configureNumRooms() {
        edit_nbr_rooms.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val nbrRoomsStr: String = edit_nbr_rooms.text.toString()
                if (nbrRoomsStr.isNotEmpty()) {
                    numberOfRooms = nbrRoomsStr.toInt()
                }
            }
        })
    }

    // Number of bed config
    private fun configureNumBed() {
        edit_nbr_bed.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val nbrBedStr: String = edit_nbr_bed.text.toString()
                if (nbrBedStr.isNotEmpty()) {
                    numberOfBed = nbrBedStr.toInt()
                }
            }
        })
    }

    // Number of bathrooms config
    private fun configureNumBath() {
        edit_nbr_bath.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val nbrBathStr: String = edit_nbr_bath.text.toString()
                if (nbrBathStr.isNotEmpty()) {
                    numberOfBath = nbrBathStr.toInt()
                }
            }
        })
    }

    // Apart number config
    private fun configureNumApart() {
        edit_apart_nbr.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val nbrApartStr: String = edit_apart_nbr.text.toString()
                if (nbrApartStr.isNotEmpty()) {
                    apartNumber = nbrApartStr.toInt()
                }
            }
        })
    }

    // Living space config
    private fun configureSurface() {
        edit_surface.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val surfaceStr: String = edit_surface.text.toString()
                if (surfaceStr.isNotEmpty()) {
                    surface = surfaceStr.toInt()
                }
            }
        })
    }


    // Spinner to select typ of properties
    private fun configureSpinner() {
        type_spinner!!.onItemSelectedListener = this
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfTypes)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        type_spinner!!.adapter = aa
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        typeOfProperty = "Manor"
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        typeOfProperty = listOfTypes[position]
        if (typeOfProperty == "Apartment") {
            apart_number.isVisible = true
            edit_apart_nbr.isVisible = true
        } else {
            apart_number.isVisible = false
            edit_apart_nbr.isVisible = false
            apartNumber = 0
        }
    }

    // Checkbox clicks
    private fun configureCheckBoxClick() {
        checkbox_airport.setOnClickListener { airport = checkbox_airport.isChecked }
        checkbox_school.setOnClickListener { school = checkbox_school.isChecked }
        checkbox_shops.setOnClickListener { shops = checkbox_shops.isChecked }
        checkbox_subway.setOnClickListener { subway = checkbox_subway.isChecked }
        checkbox_train_station.setOnClickListener {
            trainStation = checkbox_train_station.isChecked
        }
        checkbox_park.setOnClickListener { park = checkbox_park.isChecked }
        checkbox_available.setOnClickListener {
            sold = true
            checkbox_sold.isChecked = false
            soldDate = ""
            picker_sold_date.text = soldDate
        }
        checkbox_sold.setOnClickListener {
            sold = false
            checkbox_available.isChecked = false
        }
    }

    // Check storage permission
    private fun checkPermissionToReadStorage() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "sorry but you did not grant permission to the application to access the image gallery",
                Toast.LENGTH_LONG
            ).show()
        } else {
            pickImageFromGallery()
        }
    }

    // Check access camera permission
    private fun checkPermissionToAccessCamera() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "sorry but you did not grant permission to the application to access the camera",
                Toast.LENGTH_LONG
            ).show()
        } else {
            openCamera()
        }
    }

    // To open device camera
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_PICK_CODE)
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000

        //camera pick code
        private const val CAMERA_PICK_CODE = 100
    }

    // To open and get picture in gallery
    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null) {
                imageUri = data.data
                imageUri?.let { showCustomDialog(it) }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PICK_CODE) {
            Log.e("test path uri", imageUri.toString())
            if (imageUri != null) {
                imageUri?.let { showCustomDialog(it) }
            }
        }
    }

    // To show picture title dialog
    @SuppressLint("InflateParams")
    private fun showCustomDialog(image_uri: Uri) {
        var picture: Picture?
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.picture_title_dialogue, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Login Form")
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val name = mDialogView.dialogNameEt.text.toString()
            picture = Picture(0, name, image_uri.toString())
            if (picture != null) {
                this.pictures.add(picture!!)
            }
            updateUi()
        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    // Update UI
    private fun updateUi() {
        configureRecyclerView()
    }

    // Floatin Action Button valid configuration
    private fun configureButtonValidate() {
        extended_fab.setOnClickListener {
            if (Utils.isInternetAvailable(this)) {
                checkValues()
            } else {
                showAlertDialog()
            }
        }
    }

    // To save geolocation in database
    private fun storeLocationToDatabase() {
        progressBar_create.visibility = View.VISIBLE
        val addressStr = checkClass.checksAddressElements(address, city, postalCode)
        if (addressStr.isNotEmpty()) {
            val realEstateStream = RealEstateStream()
            disposable =
                realEstateStream.streamFetchGeocodeInfo(addressStr, BuildConfig.GoogleSecAPIKEY)
                    .subscribeWith(object : DisposableObserver<GeocodeInfo?>() {
                        override fun onNext(t: GeocodeInfo) {
                            geoLocation = t
                        }

                        override fun onError(e: Throwable) {
                            showAlertDialog()
                            progressBar_create.visibility = View.GONE
                        }

                        override fun onComplete() {
                            setValuesInProperty()
                        }
                    })
        }
    }

    // Get latitude/longitude from address input
    private fun getLocationFromAddress(strAddress: String?): LatLng? {

        val coder = Geocoder(this)
        val address: List<android.location.Address>?
        val latlng: LatLng?

        address = coder.getFromLocationName(strAddress, 3)
        return if (address.isEmpty()) {

            null
        } else {

            val location = address[0]
            latlng = LatLng(location.latitude, location.longitude)

            latlng
        }
    }

    // To set values in object
    private fun setValuesInProperty() {
        progressBar_create.visibility = View.GONE
        //Log.e("restls.get(0) est :  ${geoLocation.results?.indexOf(0)}","Results !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" )
        //lat = geoLocation.results?.get(0)?.geometry?.location?.lat!!
        //lng = geoLocation.results?.get(0)?.geometry?.location?.lng!!
        //lat = 49.52267
        //lng = 0.091310
        val latLng: LatLng
        latLng = this!!.getLocationFromAddress(address.toString())!!
        lat = latLng.latitude
        lng = latLng.longitude

        property = checkClass.setValuesInProperty(
            lat, lng, airport, school, subway, shops, trainStation, park, additionalAddress,
            pictures, address, description, entryDate, apartNumber, sold, soldDate, property
        )
        propertyViewModel.createProperty(property)
        showNotification()
        returnToHome()
    }

    // Show alertDialog if internet is not available
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialog_not_internet_text)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { _, _ ->
            checkValues()
        }
        //performing negative action
        builder.setNegativeButton("No") { _, _ ->
            alertDialog.dismiss()
        }
        // Create the AlertDialog
        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // Check empty values
    private fun checkValues() {
        if (checkClass.checkValueBeforeStoreProperty(
                this, typeOfProperty, surface, numberOfRooms, numberOfBed,
                numberOfBath, address, price, realtorName, entryDate, soldDate, sold, property,
                city, postalCode, country
            ).type == typeOfProperty
        ) {
            property = checkClass.checkValueBeforeStoreProperty(
                this, typeOfProperty, surface, numberOfRooms, numberOfBed,
                numberOfBath, address, price, realtorName, entryDate, soldDate, sold, property,
                city, postalCode, country
            )
            storeLocationToDatabase()
        }
    }

    // Listener to remove pictures
    private fun onItemClicked(position: Int) {
        pictures.remove(pictures[position])
        configureRecyclerView()
    }

    //To display notification
    private fun showNotification() {
        val notificationClass = NotificationClass()
        notificationClass.showNotification(this, isEdit)
    }

    //To display list with new or updated property
    private fun returnToHome() {
        if (!isEdit) {
            RealEstateManagerApplication.setLastItemClicked(-2)
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}