package com.lamine.realestatemanager.controllers.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.lamine.realestatemanager.R
import com.lamine.realestatemanager.controllers.viewModel.DataInjection
import com.lamine.realestatemanager.controllers.viewModel.PropertyViewModel

class StartUpActivity : AppCompatActivity() {

    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)

        deletIfExist()
        Handler().postDelayed({
            lanchMainActivity()
        }, 500)
    }

    private fun lanchMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun deletIfExist() {
        propertyViewModel = ViewModelProviders.of(
            this,
            this.let { DataInjection.Injection.provideViewModelFactory(it) }
        ).get(PropertyViewModel::class.java)
        Thread {
            propertyViewModel.deleteProperty(20001)
        }.start()
    }

}

