package com.lamine.realestatemanager.controllers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.activities.MainActivity
import com.lamine.realestatemanager.utils.Prefs


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    private var foreign: Boolean = false
    private lateinit var prefs: Prefs

    companion object {

        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.property_setting)
        getPrefs()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    //Request preferences
    private fun getPrefs(){
    prefs = Prefs.get(activity)
    foreign = prefs.foreignCurrency
}
    //Checkbox foreign choice configuration
    private fun initCheckBoxForeign(){
        if(foreign){

        }
    }

}