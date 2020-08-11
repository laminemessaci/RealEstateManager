package com.lamine.realestatemanager.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.fragments.DetailEstateFragment
import kotlinx.android.synthetic.main.toolbar.*

class DetailActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var propertyId: Long = 0


    companion object{
        const val PROPERTY = "property"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        configureAndShowFragmentDetail()
        configureToolbar()
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }


    private fun configureAndShowFragmentDetail() {
        val detailEstateFragment = DetailEstateFragment.newInstance(propertyId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_detail_frame_layout, detailEstateFragment).commit()
    }

}