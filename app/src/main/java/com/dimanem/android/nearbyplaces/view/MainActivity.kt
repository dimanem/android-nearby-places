package com.dimanem.android.nearbyplaces.view

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dimanem.android.nearbyplaces.R
import com.dimanem.android.nearbyplaces.view.list.NearbyPlacesListFragment
import com.dimanem.android.nearbyplaces.view.location.LocationObserver
import com.dimanem.android.nearbyplaces.view.location.LocationUtil
import com.dimanem.android.nearbyplaces.view.map.NearbyPlacesMapFragment
import com.dimanem.android.nearbyplaces.viewmodel.NearbyPlacesViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var locationUtil: LocationUtil

    var viewModel: NearbyPlacesViewModel? = null

    // View Switching
    var menu: Menu? = null
    var showingMap: Boolean? = null

    // TODO use dependency injection
    private lateinit var locationObserver: LocationObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NearbyPlacesViewModel::class.java)

        // Location observer
        locationObserver = LocationObserver(application, lifecycle, locationUtil, object : LocationObserver.Callback {
            override fun onLocationChanged(location: Location) {
                viewModel?.setCurrentLocation(location)
            }
        })
        if (locationUtil.isPermissionGranted()) {
            locationObserver.enable()
        } else {
            requestLocationPermission()
        }
        lifecycle.addObserver(locationObserver)

        // Show the list first
        showListFragment()
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Timber.e("Location permission not granted!")
                    return
                }

                // Enable location manager
                locationObserver.enable()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu

        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_switch_view -> {
                if (showingMap == true) {
                    showListFragment()
                } else {
                    showMapFragment()
                }
                return true
            }

            // TODO support settings
            R.id.action_settings ->
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true

            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }

    private fun showListFragment() {
        showingMap = false
        menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_map_black_24dp)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, NearbyPlacesListFragment())
                .commit()
    }

    private fun showMapFragment() {
        showingMap = true
        menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_list_black_24dp)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, NearbyPlacesMapFragment())
                .commit()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }
}
