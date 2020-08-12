package com.lamine.realestatemanager.controllers.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.Property
import kotlinx.android.synthetic.main.fragment_detail_estate.*
import java.text.NumberFormat


class DetailEstateFragment : Fragment(), OnMapReadyCallback {

    private lateinit var property: Property
    private var propertyId: Long = 0
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mListner: EstateListFragment.OnFragmentInteractionListener? = null
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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_estate, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detail_lite_map.onCreate(savedInstanceState)
        detail_lite_map.getMapAsync(this)
        initViewModelFactory()
        getTheBundle()

    }

    private fun getTheBundle() {
        if (arguments != null) {
            propertyId != arguments?.getLong(ARG_PARAM)!!
            if (propertyId != 0L) {
                propertyViewModel.getProperty(propertyId).observe(this, Observer { property ->
                    property?.let { initVars(it) }
                })
            } else {
                propertyViewModel.getAllProperty().observe(this, Observer { updateAdapterWithDefaultValue(it!!) })

            }
        }
    }

    private fun updateAdapterWithDefaultValue(properties: List<Property>) {

        // TO DO
    }

    @SuppressLint("SetTextI18n")
    private fun initVars(property: Property) {
        this.property = property
        text_type.text = property.type
        if (property.description != null) tv_description_text.text = property.description
        if(property.livingSpace != null)  text_surface.text = property.livingSpace.toString()
        if (property.rooms != null) text_nbr_of_rooms.text = property.rooms.toString()
        if (property.numOfBath != null)text_nbr_bathrooms.text = property.numOfBath.toString()
        if (property.numOfBed != null)text_nbr_bedrooms.text = property.numOfBed.toString()
        if (property.address?.address != null)text_location_num_street.text = property.address?.address
        if (property.address?.city != null)text_location_town.text = property.address?.city
        if (property.address?.country != null)text_location_country.text = property.address?.country
        if (property.address?.postalCode != null)text_location_cp.text = property.address?.postalCode
        text_price.text = currencyFormat.format(property.price)
        if (property.address?.apartmentNumber != 0) {
            text_location_num_type.text =
                resources.getString(R.string.nbr_of_apart) + property.address?.apartmentNumber.toString()
        } else {
            text_location_num_type.visibility = View.GONE
        }
        if(property.address?.additionalAddress?.isNotEmpty()!!){
            text_location_additional.text = property.address?.additionalAddress
        }else{
            text_location_additional.visibility = View.GONE
        }
        if (property.realtor != null)text_realtor.text = property.realtor
        initCheckbox()
        initStatus()
    }
    private fun initStatus() {
        if(property.status!!){
            checkboxAvailable.isChecked = true
        }else{
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



    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this, activity?.applicationContext?.let {
            DataInjection.Injection.provideViewModelFactory(
                it
            )
        }).get(PropertyViewModel::class.java)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
      if(googleMap != null){
          map = googleMap
      }
    }

}