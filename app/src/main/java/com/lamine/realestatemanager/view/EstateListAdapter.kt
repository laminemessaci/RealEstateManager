package com.lamine.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.lamine.realestatemanager.models.Property

/**
 *Created by Lamine MESSACI on 01/08/2020.
 */
class EstateListAdapter(private val list: List<Property>, private val clickListener: (Property) -> Unit)
    : RecyclerView.Adapter<EstateListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateListViewHolder {
        TODO("Not yet implemented")
        val inflater = LayoutInflater.from(parent.context)
        return EstateListViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {return list.size}

    override fun onBindViewHolder(holder: EstateListViewHolder, position: Int) {
        val property: Property = list[position]
        holder.bind(property, clickListener, position)
    }
}