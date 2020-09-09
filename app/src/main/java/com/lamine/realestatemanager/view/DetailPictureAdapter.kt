package com.lamine.realestatemanager.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lamine.realestatemanager.models.Picture

/**
 *Created by Lamine MESSACI on 06/08/2020.
 */
class DetailPictureAdapter(
    private val list: List<Picture>,
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<DetailPictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DetailPictureViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DetailPictureViewHolder, position: Int) {
        val picture: Picture = list[position]
        holder.bind(picture, clickListener)
    }

    override fun getItemCount(): Int = list.size
}