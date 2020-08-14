package com.lamine.realestatemanager.controllers.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

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


}