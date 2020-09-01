package com.lamine.realestatemanager.controllers.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.lamine.realestatemanager.BuildConfig
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.activities.MainActivity
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.GeocodeInfo
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.RealEstateStream
import com.lamine.realestatemanager.utils.Utils
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var mListener: OnMapsFragmentListener? = null
    private lateinit var propertiesList: List<Property>
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var disposable: Disposable
    private lateinit var geoCodeList: MutableList<GeocodeInfo>
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geoLocation: GeocodeInfo
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var marker: Marker
    private var initPosition: Boolean = false
    private var mLocationRequest = LocationRequest()

    companion object {
        fun newInstance(): MapsFragment {
            return MapsFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMapsFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnMapsFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.property_location)
        getLocationClient()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val map =
            childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        map?.getMapAsync(this)
        initViewModel()
    }

    // Request user location
    private fun getLocationClient() {
        fusedLocationClient =
            activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
    }

    // Init ViewModel
    private fun initViewModel() {
        propertyViewModel = ViewModelProviders.of(
            this,
            context?.let { DataInjection.Injection.provideViewModelFactory(it) }
        ).get(PropertyViewModel::class.java)
        propertyViewModel.getAllProperty()
            .observe(this, Observer { displayList(it!!) })
    }

    // To display items on map
    private fun displayList(properties: List<Property>) {
        geoCodeList = arrayListOf()
        propertiesList = properties
        for (p: Property in properties) {
            if (p.address?.lat != 0.0 && p.address?.lng != 0.0) {
                addMarker(p, p.address?.lat!!, p.address?.lng!!)
            } else {
                if (activity?.let { Utils.isInternetAvailable(it) }!!) {
                    executeRequestToGetAddresses(p)
                } else {
                    Toast.makeText(
                        activity,
                        "Sorry but internet isn't available. We can't get addresses for properties.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // To request location if not exist in database
    private fun executeRequestToGetAddresses(property: Property) {
        val addressStr =
            property.address?.address + "+" + property.address?.city + property.address?.postalCode /*+ "+" + property.address?.additionalAddress +"+"+ property.address?.apartmentNumber +" "+ property.address?.sector*/
        Log.e("test address", addressStr)
        val realEstateStream = RealEstateStream()
        disposable =
            realEstateStream.streamFetchGeocodeInfo(addressStr, BuildConfig.GoogleSecAPIKEY)
                .subscribeWith(object : DisposableObserver<GeocodeInfo?>() {
                    override fun onNext(t: GeocodeInfo) {
                        geoLocation = t
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                        geoCodeList.add(geoLocation)
                        val lat: Double = geoLocation.results?.get(0)?.geometry?.location?.lat!!
                        val lng: Double = geoLocation.results?.get(0)?.geometry?.location?.lng!!
                        addMarker(property, lat, lng)
                        storeLocation(property, lat, lng)
                    }
                })
    }

    // Store location in database
    private fun storeLocation(property: Property, lat: Double, lng: Double) {
        property.address?.lat = lat
        property.address?.lng = lng
        propertyViewModel.updateProperty(property)
    }

    // To add markers on map
    private fun addMarker(property: Property, lat: Double, lng: Double) {
        val latLon = LatLng(lat, lng)
        map.addMarker(
            MarkerOptions().position(latLon).title(property.type)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(latLon))
        gotoMyLocation()
    }

    // Map root
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
            if (activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && activity?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.setMyLocationEnabled(true)
        }
    }

    // For refresh location
    private fun gotoMyLocation() {
        requestNewLocationData()
    }

    // To refresh map
    private fun getLocation(location: Location) {
        if (initPosition) marker.remove()
        val latLon: LatLng
        val latitude: Double?
        val longitude: Double?
        latitude = location.latitude
        longitude = location.longitude
        latLon = LatLng(latitude, longitude)
        marker = map.addMarker(latLon.let {
            MarkerOptions().position(it).title("Me")
        })
        map.setOnMarkerClickListener(this)
        if (!initPosition) {
            map.moveCamera(CameraUpdateFactory.newLatLng(latLon))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 15.0F))
            map.uiSettings.isZoomControlsEnabled = true
            map.setOnMarkerClickListener(this)
            initPosition = true
        }
    }

    // Request new location
    private fun requestNewLocationData() {
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    getLocation(location)
                    if (progressBar != null) {
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = (30 * 1000)
        mLocationRequest.fastestInterval = 1000
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    // Markers listener
    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.let { getPropertyId(it) }
        return true
    }

    // Get propertyId from Marker
    private fun getPropertyId(marker: Marker) {
        marker.showInfoWindow()
        map.moveCamera(CameraUpdateFactory.newLatLng(marker.position))
        for (p: Property in propertiesList) {
            val latLng: LatLng =
                p.address?.lat?.let { p.address!!.lng?.let { it1 -> LatLng(it, it1) } }!!
            if (marker.position == latLng) {
                mListener?.onMapsInteraction(p)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    // To stop locations callback
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    // Maps interface
    interface OnMapsFragmentListener {
        fun onMapsInteraction(property: Property)
    }

}
