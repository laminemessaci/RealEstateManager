package com.lamine.realestatemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.models.Property

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EstateListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EstateListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estate_list, container, false)
    }

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
    }
}