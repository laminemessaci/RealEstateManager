package com.lamine.realestatemanager.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lamine.realestatemanager.R

class DetailActivity : AppCompatActivity() {

    companion object{
        const val PROPERTY = "property"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}