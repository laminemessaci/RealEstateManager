package com.lamine.realestatemanager.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.RealEstateManagerApplication
import com.lamine.realestatemanager.models.Property
import com.lamine.realestatemanager.utils.Prefs
import kotlinx.android.synthetic.main.estate_list_item.view.*
import java.text.NumberFormat
import java.util.*

/**
 *Created by Lamine MESSACI on 01/08/2020.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EstateListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.estate_list_item, parent, false)) {

    private val mTypeView = itemView.estate_type
    private val mTownView = itemView.estate_town
    private var mPriceView = itemView.estate_price
    private var mImageView = itemView.estate_picture
    private var mSoldTextView = itemView.estate_sold
    private var constraint = itemView.constraint_item
    private val glide: RequestManager = Glide.with(itemView)
    private var index: Int = 0
    private lateinit var currencyFormat: NumberFormat

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(property: Property, clickListener: (Property) -> Unit, position: Int) {
        val prefs:Prefs= Prefs.get(RealEstateManagerApplication.getContext())
        currencyFormat =
            if(prefs.foreignCurrency) NumberFormat.getCurrencyInstance(Locale.FRANCE) else NumberFormat.getCurrencyInstance(Locale.US)
        currencyFormat.maximumFractionDigits = 0

        index = RealEstateManagerApplication.getLastItemClicked()

        mTypeView?.text = property.type
        mTownView?.text = property.address?.city
        mPriceView?.text = currencyFormat.format(property.price)
        //get image from storage
        if (property.pictures != null && property.pictures!!.isNotEmpty()) {
            Log.e("test estate list vh", (property.pictures?.get(0)?.picturePath))
            mImageView?.let {
                glide.load(Uri.parse(property.pictures?.get(0)?.picturePath))
                    .apply(RequestOptions().centerCrop()).into(
                        it
                    )
            }
        }
        if (property.status == false) {
            mSoldTextView.visibility = View.VISIBLE
        }
        itemView.setOnClickListener {
            if(index!=-1) RealEstateManagerApplication.setLastItemClicked(position)
            clickListener(property)
        }
        if(index!=-1)setItemColor(position)
    }

    //To set item and view color
    private fun setItemColor(position: Int) {
        if (index == position) {
            constraint.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorGreen
                )
            )
            mPriceView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
        } else {
            constraint.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.white
                )
            )
            mPriceView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorPrimaryDark
                )
            )
        }
    }

}