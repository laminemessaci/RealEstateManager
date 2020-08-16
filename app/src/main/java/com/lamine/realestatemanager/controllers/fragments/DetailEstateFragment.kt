package com.lamine.realestatemanager.controllers.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.controllers.activities.CreateEstateActivity
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.Prefs
import com.lamine.realestatemanager.view.DetailPictureAdapter
import kotlinx.android.synthetic.main.fragment_detail_estate.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*


class DetailEstateFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var property: Property
    private var propertyId: Long = 0
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mListener: OnFragmentDetailListener? = null
    private lateinit var currencyFormat: NumberFormat




    companion object {
        private const val ARG_PARAM = "property"
        fun newInstance(propertyId: Long): DetailEstateFragment {
            val fragment = DetailEstateFragment()
            val args = Bundle()
            args.putLong(ARG_PARAM, propertyId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_detail_estate, container, false)


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detail_lite_map.onCreate(savedInstanceState)
        detail_lite_map.getMapAsync(this)
        initViewModelFactory()
        getTheBundle()
        getForeign()
    }


    // Get intent data
    private fun getTheBundle() {
        if (arguments != null) {
            propertyId = arguments?.getLong(ARG_PARAM)!!
            if (propertyId != 0L) {
                propertyViewModel.getProperty(propertyId).observe(this, Observer { property ->
                    property?.let { initVars(it) }
                })
            } else {
                propertyViewModel.getAllProperty()
                    .observe(this, Observer { updateAdapterWithDefaultValue(it!!) })
            }
        }
    }

    private fun getForeign() {
        val prefs: Prefs = Prefs.get(RealEstateManagerApplication.getContext())
        currencyFormat =
            if (prefs.foreignCurrency) NumberFormat.getCurrencyInstance(Locale.FRANCE) else NumberFormat.getCurrencyInstance(
                Locale.US
            )
    }

    // To set data in views
    @SuppressLint("SetTextI18n")
    private fun initVars(property: Property) {
        this.property = property
        if (property.pictures != null) {

        }
        text_type.text = property.type
        if (property.description != null) tv_description_text.text = property.description
        if (property.livingSpace != null) text_surface.text = property.livingSpace.toString()
        if (property.rooms != null) text_nbr_of_rooms.text = property.rooms.toString()
        if (property.numOfBath != null) text_nbr_bathrooms.text = property.numOfBath.toString()
        if (property.numOfBed != null) text_nbr_bedrooms.text = property.numOfBed.toString()
        if (property.address?.address != null) text_location_num_street.text =
            property.address?.address
        if (property.address?.city != null) text_location_town.text = property.address?.city
        if (property.address?.country != null) text_location_country.text =
            property.address?.country
        if (property.address?.postalCode != null) text_location_cp.text =
            property.address?.postalCode
        text_price.text = currencyFormat.format(property.price)
        if (property.address?.apartmentNumber != 0) {
            text_location_num_type.text =
                resources.getString(R.string.nbr_of_apart) + property.address?.apartmentNumber.toString()
        } else {
            text_location_num_type.visibility = View.GONE
        }
        if (property.address?.additionalAddress?.isNotEmpty()!!) {
            text_location_additional.text = property.address?.additionalAddress
        } else {
            text_location_additional.visibility = View.GONE
        }
        if (property.realtor != null) text_realtor.text = property.realtor
        initCheckbox()
        initStatus()
        configureButtonEdit()
        configureRecyclerView()
        addMarkers()
    }

    private fun initStatus() {
        if (property.status!!) {
            checkboxAvailable.isChecked = true
        } else {
            checkboxSold.isChecked = true
            tv_soldDate.visibility = View.VISIBLE
            picker_soldDate.visibility = View.VISIBLE
            picker_soldDate.text = property.dateOfSale
        }
        picker_entryDate.text = property.dateOfEntry
        checkboxAvailable.isEnabled = false
        checkboxSold.isEnabled = false
    }

    // To init checkbox
    private fun initCheckbox() {
        if (property.airport!!) {
            checkbox_airport.isChecked = true
        }
        if (property.school!!) {
            checkbox_school.isChecked = true
        }
        if (property.shops!!) {
            checkbox_shops.isChecked = true
        }
        if (property.subway!!) {
            checkbox_subway.isChecked = true
        }
        if (property.trainStation!!) {
            checkbox_train_station.isChecked = true
        }
        if (property.park!!) {
            checkbox_park.isChecked = true
        }
        checkbox_airport.isEnabled = false
        checkbox_school.isEnabled = false
        checkbox_shops.isEnabled = false
        checkbox_subway.isEnabled = false
        checkbox_train_station.isEnabled = false
        checkbox_park.isEnabled = false
    }

    private fun configureButtonEdit() {
        extended_fab_edit.setOnClickListener {
            launchCreateActivity(propertyId)

        }
    }

    // To launch CreateActivity
    private fun launchCreateActivity(id: Long?) {
        val intent = Intent(activity, CreateEstateActivity::class.java)
        if (id != null) {
            intent.putExtra("property", propertyId)
        }
        startActivity(intent)
    }

    // To configure recyclerView
    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        detail_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
            adapter =
                DetailPictureAdapter(property.pictures!!) { position: Int -> onItemClicked(position) }
        }
    }

    // ViewModel initialisation
    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
            activity?.applicationContext?.let {
                DataInjection.Injection.provideViewModelFactory(
                    it
                )
            }).get(PropertyViewModel::class.java)
    }

    // Update adapter with default value
    private fun updateAdapterWithDefaultValue(properties: List<Property>) {
        if (properties.isNotEmpty()) {
            property = properties[0]
            configureRecyclerView()
            initVars(property)
        }
    }

    // Map configuration
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }
    }

    // Add marker on map
    private fun addMarkers() {
        fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        val myPlace =
            property.address?.lat?.let { property.address!!.lng?.let { it1 -> LatLng(it, it1) } }
        map.addMarker(myPlace?.let { MarkerOptions().position(it).title(property.type) })
        map.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
    }

    // Markers listener
    override fun onMarkerClick(marker: Marker?): Boolean {
        mListener?.onDetailInteraction(property)
        return true
    }

    // DetailFragment Callback
    interface OnFragmentDetailListener {
        fun onDetailInteraction(property: Property)
    }

    private fun onItemClicked(position: Int) {
        Log.d("test", position.toString())
    }

}