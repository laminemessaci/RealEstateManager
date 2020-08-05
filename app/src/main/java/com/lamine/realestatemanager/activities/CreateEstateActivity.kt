package com.lamine.realestatemanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lamine.realestatemanager.R

class CreateEstateActivity : AppCompatActivity() {

    //----------FOR DATA--------------//
    private var propertyId: Long = 0
    private var surface: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)

    }
}