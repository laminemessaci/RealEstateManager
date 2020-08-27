package com.lamine.realestatemanager.controllers.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.controllers.activities.MainActivity
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.SearchUtils
import com.lamine.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {

    val listOfSearchTypes =
        arrayOf("ALL", "Manor", "House", "Castle", "Flat", "Loft", "Apartment", "Duplex")

    private var mListener: OnSearchFragmentListener? = null
    private lateinit var propertyViewModel: PropertyViewModel
    private var typeOfProperty: String = "ALL"
    private var roomMin: Int = 0
    private var roomMax: Int = 40
    private var surfaceMin: Int = 0
    private var surfaceMax: Int = 1000
    private var priceMin: Double = 0.0
    private var priceMax: Double = 0.0
    private var bedRoomsMin: Int = 0
    private var bedRoomsMax: Int = 20
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private var entryDate: String = ""
    private var maxDate: String = ""
    private var city: String = ""
    private var postalCode: String = ""
    private var country: String = ""
    private var sold: Boolean = false
    private var available: Boolean = false
    private var airport: Boolean = false
    private var school: Boolean = false
    private var subway: Boolean = false
    private var shops: Boolean = false
    private var trainStation: Boolean = false
    private var park: Boolean = false
    private var numberOfBath: Int = 0
    private var realtorName: String = ""
    private var numberOfImages: Int = 0

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnSearchFragmentListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.property_search)
        initViewModel()
        configureTypes()
        configureEditTown()
        configurePostalCode()
        configureCountry()
        configureCheckBox()
        configureDateEntry()
        configureDateSold()
        configureRealtorName()
        configureNbrBathrooms()
        configureNbrOfImages()
        configureSeekBarSurface()
        configureSeekBarNumbRooms()
        configureSeekBarPrice()
        configureButtonSearch()
        configureSeekBarBeds()
    }

    // ViewModel initialisation
    private fun initViewModel() {
        propertyViewModel = ViewModelProviders.of(
            this,
            context?.let { DataInjection.Injection.provideViewModelFactory(it) }
        ).get(PropertyViewModel::class.java)
    }

    // To configure asked surface
    private fun configureSeekBarSurface() {
        seekBar_surface.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            getEditTextFocus()
            tvSurfaceResultMin.text = minValue.toString()
            tvSurfaceResultMax.text = maxValue.toString()
        }
    }

    private fun getEditTextFocus() {
        when {
            edit_town.hasFocus() -> edit_town.clearFocus()
            edit_postl_code.hasFocus() -> edit_postl_code.clearFocus()
            edt_country.hasFocus() -> edt_country.clearFocus()
        }
    }

    // To configure asked bathrooms number
    private fun configureNbrBathrooms() {
        edit_bath.editText?.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val nbrBathStr: String = edit_bath.editText?.text.toString()
                if (nbrBathStr.isNotEmpty()) {
                    numberOfBath = nbrBathStr.toInt()
                }
            }
        })
    }

    // To configure asked realtor name
    private fun configureRealtorName() {
        edt_realtor.editText?.addTextChangedListener(object : TextWatcher {
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
                realtorName = edt_realtor.editText?.text.toString().replace(" ", "")
            }
        })
    }

    // To configure asked sold date
    private fun configureDateSold() {
        picker_sold.editText?.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { _, year, monthOfYear, dayOfMonth ->
                        /* Display Selected date in TextView */
                        picker_sold.editText?.setText(Utils.getStringDate(year, dayOfMonth, monthOfYear))
                        maxDate = picker_sold.editText?.text.toString()
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }
    }

    // To configure asked entry date
    private fun configureDateEntry() {
        picker_entry.editText?.setText (Utils.getTodayDate())
        picker_entry.editText?.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { _, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        picker_entry.editText?.setText( Utils.getStringDate(year, dayOfMonth, monthOfYear))
                        entryDate = picker_entry.editText?.text.toString()
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }
    }

    // To configure asked bed number
    private fun configureSeekBarBeds() {
        seekBar_nbr_bed.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            getEditTextFocus()
            bedroom_min.text = minValue.toString()
            bedroom_max.text = maxValue.toString()
        }
    }

    // To configure asked CheckBox
    private fun configureCheckBox() {
        check_airport.setOnClickListener { airport = true }
        check_school.setOnClickListener { school = true }
        check_shops.setOnClickListener { shops = true }
        check_subway.setOnClickListener { subway = true }
        check_train_station.setOnClickListener { trainStation = true }
        check_park.setOnClickListener { park = true }

        check_available.setOnClickListener {
            available = check_available.isChecked
            check_sold.isChecked = false
        }
        check_sold.setOnClickListener {
            check_available.isChecked = false
            sold = check_sold.isChecked
        }
    }

    // To configure country editText
    private fun configureCountry() {

        edt_country.editText?.doOnTextChanged{_,_,_,_->
            country = edt_country.editText?.text.toString().replace(" ", "")
        }
    }

    // To configure postal code editText
    private fun configurePostalCode() {

        edit_postl_code.editText?.doOnTextChanged{_,_,_,_->
            postalCode = edit_postl_code.editText?.text.toString().replace(" ", "")
        }
    }

    // To configure Price SeekBar
    private fun configureSeekBarPrice() {
        seekBar_price.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            getEditTextFocus()
            price_min.text = minValue.toString()
            price_max.text = maxValue.toString()
        }
    }

    // To configure town editText
    private fun configureEditTown() {

        edit_town.editText?.doOnTextChanged{_,_,_,_->
            city = edit_town.editText?.text.toString().replace(" ", "")
        }
    }

    // To configure images number editText
    private fun configureNbrOfImages() {
        edit_nbr_images.editText?.addTextChangedListener(object : TextWatcher {
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
                val nbrImgStr: String = edit_nbr_images.editText?.text.toString()
                if (nbrImgStr.isNotEmpty()) {
                    numberOfImages = nbrImgStr.toInt()
                }
            }
        })
    }

    // To configure rooms SeekBar
    private fun configureSeekBarNumbRooms() {
        seekBar_nbr_rooms.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            getEditTextFocus()
            room_min.text = minValue.toString()
            room_max.text = maxValue.toString()
        }
    }

    // To configure spinner Types
    private fun configureTypes() {
        type_spinner!!.onItemSelectedListener = this
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa =
            activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    listOfSearchTypes
                )
            }
        // Set layout to use when the list of choices appear
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        type_spinner!!.adapter = aa
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        typeOfProperty = listOfSearchTypes[position]
    }

    // Search Button config
    private fun configureButtonSearch() {
        button_search.setOnClickListener {
            getEntryValues()
        }
    }

    // get values
    private fun getEntryValues() {
        surfaceMin = Integer.decode(tvSurfaceResultMin.text.toString())
        surfaceMax = Integer.decode(tvSurfaceResultMax.text.toString())
        roomMin = Integer.decode(room_min.text.toString())
        roomMax = Integer.decode(room_max.text.toString())
        priceMin = price_min.text.toString().toDouble()
        priceMax = price_max.text.toString().toDouble()
        bedRoomsMin = Integer.decode(bedroom_min.text.toString())
        bedRoomsMax = Integer.decode(bedroom_max.text.toString())

        makeSearchQuery()
    }

    // --- Make Search -- //
    private fun makeSearchQuery() {
        val searchUtils = SearchUtils()
        val query = searchUtils.makeQuery(
            typeOfProperty,
            surfaceMin,
            surfaceMax,
            roomMin,
            roomMax,
            city,
            postalCode,
            country,
            shops,
            airport,
            park,
            subway,
            school,
            trainStation,
            sold,
            available,
            priceMin,
            priceMax,
            bedRoomsMin,
            bedRoomsMax,
            entryDate,
            maxDate,
            realtorName,
            numberOfBath,
            numberOfImages
        )

        Log.e("***test args: ", query)
        propertyViewModel.getPropertyByArgs(query).observe(this, androidx.lifecycle.Observer {
            if (it!!.isNotEmpty()) {
                getResult(it)
            } else {
                Toast.makeText(
                    this.context!!,
                    resources.getString(R.string.result_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    // Get search result
    private fun getResult(it: List<Property>) {
        RealEstateManagerApplication.setSearchCalls(true)
        mListener?.onSearchInteraction(it)
    }

    // Search interface
    interface OnSearchFragmentListener {
        fun onSearchInteraction(it: List<Property>)
    }

}