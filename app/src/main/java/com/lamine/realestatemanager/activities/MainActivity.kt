package com.lamine.realestatemanager.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.lamine.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var  drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var isTablet:Boolean = false
    private var isDisplaySearch = false

     val FRAGMENT_LIST = "listFragment"
     val FRAGMENT_MAP = "mapsFragment"
     val FRAGMENT_DETAIL = "DetailEstateFragment"
     val FRAGMENT_SETTINGS = "SettingsFragment"
     val DATA = "data"
     val LOCATION = "location"
     val INTERNET = "internet"
     val OPEN_MAPS = "open_maps"
     val FRAGMENT_MORT_GAGE = "mortGageCalculatorFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureToolbar()
        defineIsTablet()
        configureNavDrawer()


    }

    //check if device is a tablet
    private fun defineIsTablet() {
        if(activity_main_detail_frame_layout != null){
            isTablet = true
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Navigation view configuration
    private fun configureNavView(){
       val navigationView: NavigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)
    }


    // Toolbar menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_map -> {

                return true
            }
            R.id.menu_search -> {
                // Open search fragment
                return true
            }
            R.id.menu_create -> {
                // Open create activity

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }


}