package com.lamine.realestatemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var  drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureToolbar()
        configureNavDrawer()


    }

// Navigation Drawer Configuration
    private fun configureNavDrawer() {
        drawer = activity_main_drawer_layout
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            (R.string.navigation_drawer_open),
            (R.string.navigation_drawer_close))
    drawer.addDrawerListener(toggle)
    supportActionBar?.setHomeButtonEnabled(true)
    toggle.isDrawerIndicatorEnabled = true
    toggle.syncState()
    }

    // Toolbar configuration
    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }
}