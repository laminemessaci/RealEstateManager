package com.lamine.realestatemanager.controllers.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.controllers.activities.CreateEstateActivity
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.Utils
import com.lamine.realestatemanager.view.EstateListAdapter
import com.lamine.realestatemanager.view.ListPaddingDecoration
import kotlinx.android.synthetic.main.fragment_estate_list.*
import kotlinx.android.synthetic.main.fragment_estate_list.fab_add_property


class EstateListFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var estateListAdapter: EstateListAdapter
    private var listProperty: List<Property>? = null


    companion object {
        private const val ARG_PARAM = "any"
        fun newInstance(list: List<Property>?): EstateListFragment {
            return EstateListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, Gson().toJson(list))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_estate_list, container, false)

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTheBundleIfExist()
        fab_add_property.setOnClickListener() {
            // Open create activity
            launchCreateActivity()
        }
    }
    // To launch CreateActivity
    private fun launchCreateActivity() {
        val intent = Intent(activity, CreateEstateActivity::class.java)
        startActivity(intent)
    }

    // Get intent data
    private fun getTheBundleIfExist() {
        if (arguments != null) {
            listProperty = Gson().fromJson(
                arguments?.getString(ARG_PARAM),
                object : TypeToken<List<Property>>() {}.type
            )
            if (listProperty != null) {
                setListInRecyclerView()
            } else {
                initViewModelFactory()
                getPropertiesAndConfigureRecyclerView()
            }
        } else {
            initViewModelFactory()
            getPropertiesAndConfigureRecyclerView()
        }
    }

    // Configure recycler view
    private fun setListInRecyclerView() {
        estate_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listProperty?.let {
                EstateListAdapter(it) { property: Property ->
                    onItemClicked(property)
                }
            }
            estateListAdapter = adapter as EstateListAdapter
        }
        addItemDecoration()
    }

    // Get properties
    private fun getPropertiesAndConfigureRecyclerView() {
        // Observe the model
        propertyViewModel.getAllProperty().observe(this, Observer { displayList(it!!)})
    }

    // To set list of properties in recyclerView
    private fun displayList(properties: List<Property>) {
        listProperty = properties
        val recycler: RecyclerView = estate_picture_recycler_view
        // Data bind the recycler view
        recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =
                EstateListAdapter(properties) { property: Property -> onItemClicked(property) }
            estateListAdapter = adapter as EstateListAdapter
        }
        addItemDecoration()
        if(!RealEstateManagerApplication.isSearch()){
            recycler.post {
                recycler.scrollToPosition(Utils.getPropertyPosition(listProperty))
            }
        }else{
            RealEstateManagerApplication.setSearchCalls(false)
        }
    }

    // To add decorations to recyclerView items
    private fun addItemDecoration() {
        estate_picture_recycler_view.addItemDecoration(
            ListPaddingDecoration(
                activity as Activity,
                resources.getDimension(R.dimen.my_value),
                0
            )
        )
    }

    //Init viewModel
    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
            activity?.applicationContext?.let {
                DataInjection.Injection.provideViewModelFactory(
                    it
                )
            })
            .get(PropertyViewModel::class.java)
    }

    // Items listener
    private fun onItemClicked(property: Property) {
        mListener?.onFragmentListInteraction(property)
        if (RealEstateManagerApplication.getLastItemClicked()!= -1) {
            estateListAdapter.notifyAdapter()
        }
    }

    // ListFragment callback
    interface OnFragmentInteractionListener {
        fun onFragmentListInteraction(property: Property)
    }

}
