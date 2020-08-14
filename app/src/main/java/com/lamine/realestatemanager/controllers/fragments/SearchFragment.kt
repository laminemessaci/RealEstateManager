package com.lamine.realestatemanager.controllers.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(){

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
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
        when{
            edit_town.hasFocus() -> edit_town.clearFocus()
            edit_postl_code.hasFocus() -> edit_postl_code.clearFocus()
            edt_country.hasFocus() -> edt_country.clearFocus()
        }
    }
    // To configure asked bathrooms number
    private fun configureNbrBathrooms() {
        edit_bath.addTextChangedListener(object : TextWatcher {

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
                val nbrBathStr: String = edit_bath.text.toString()
                if (nbrBathStr.isNotEmpty()) {
                    numberOfBath = nbrBathStr.toInt()
                }
            }
        })
    }

    // To configure asked realtor name
    private fun configureRealtorName() {
        edt_realtor.addTextChangedListener(object : TextWatcher {
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
                realtorName = edt_realtor.text.toString().replace(" ", "")
            }
        })
    }
    // To configure asked sold date
    private fun configureDateSold() {
        picker_sold.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        /* Display Selected date in TextView */
                        picker_sold.text = Utils.getStringDate(year, dayOfMonth, monthOfYear)
                        maxDate = picker_sold.text.toString()
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
        picker_entry.text = Utils.getTodayDate()
        picker_entry.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        picker_entry.text = Utils.getStringDate(year, dayOfMonth, monthOfYear)
                        entryDate = picker_entry.text.toString()
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
        edt_country.addTextChangedListener(object : TextWatcher {
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
                country = edt_country.text.toString().replace(" ", "")
            }
        })
    }

    // To configure postal code editText
    private fun configurePostalCode() {
        edit_postl_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                postalCode = edit_postl_code.text.toString().replace(" ", "")
            }
        })
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
        edit_town.addTextChangedListener(object : TextWatcher {
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
                city = edit_town.text.toString().replace(" ", "")
            }
        })
    }

}