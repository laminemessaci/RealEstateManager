package com.lamine.realestatemanager.view

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lamine.realestatemanager.models.Property

/**
 *Created by Lamine MESSACI on 01/08/2020.
 */
class EstateListAdapter(private val list: List<Property>, private val clickListener: (Property) -> Unit)
    : RecyclerView.Adapter<EstateListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EstateListViewHolder(inflater, parent)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: EstateListViewHolder, position: Int) {
        val property: Property = list[position]
        holder.bind(property, clickListener, position)
//        notifyAdapter()

    }

    override fun getItemCount(): Int = list.size

    fun notifyAdapter(){
        notifyDataSetChanged()
    }

}