package com.lamine.realestatemanager.view

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.models.Property
import kotlinx.android.synthetic.main.estate_list_item.view.*
import java.text.NumberFormat
import java.util.*

/**
 *Created by Lamine MESSACI on 01/08/2020.
 */
class EstateListViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.estate_list_item, parent, false)){

    private val mTypeView = itemView.estate_type
    private val mTownView = itemView.estate_town
    private val mpriceView= itemView.estate_price
    private var mImageView= itemView.estate_picture
    private var mSoldeTextView= itemView.estate_sold
    private var constraint= itemView.constraint_item
    private val glide: RequestManager = Glide.with(itemView)
    private var index: Int = 0

    private lateinit var currencyFormat: NumberFormat

    fun bind(property: Property, clickListener: (Property) ->Unit, position: Int) {

        //TODO ->
        // Currency Format dollar | euro

        index = RealEstateManagerApplication.getLastItemClicked()
        mTownView?.text = property.type
        mTownView?.text = property.address?.city
        mpriceView?.text = currencyFormat.format(property.price)

        //Get Image from storage
        if(property.pictures != null && property.pictures!!.isNotEmpty()){
            Log.e("testing estate list !! ", property.pictures?.get(0)?.picturePath)
            mImageView?.let {
                glide.load(Uri.parse(property.pictures?.get(0)?.picturePath))
                    .apply(RequestOptions().centerCrop()).into(it)
            }
        }

        //TODO ->
        // Mange statu of Property (Sale OR Not)
        // To set Item and View Color of Status



    }

}