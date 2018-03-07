package com.dimanem.android.nearbyplaces.location

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LiveData
import android.location.Location
import com.dimanem.android.nearbyplaces.view.location.LocationUtil
import com.google.android.gms.location.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dmitri.n on 3/7/18.
 */
@Singleton
class LocationLiveData @Inject constructor(val application: Application,
                                           private val locationUtil: LocationUtil,
                                           private val fusedLocationProviderClient: FusedLocationProviderClient,
                                           private val locationRequest: LocationRequest) :
        LiveData<Location>() {

    private var locationCallback = MyLocationCallback()

    private var isPermissionGranted = false

    @SuppressLint("MissingPermission")
    override fun onActive() {
        if (isPermissionReallyGranted()) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onInactive() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun setLocationPermissionGranted(isGranted: Boolean) {
        isPermissionGranted = isGranted
        if (isPermissionReallyGranted() && hasActiveObservers()) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun isPermissionReallyGranted() = isPermissionGranted && locationUtil.isPermissionGranted()

    inner class MyLocationCallback : LocationCallback() {

        override fun onLocationAvailability(p0: LocationAvailability?) {
            // Nothing to do here...
        }

        override fun onLocationResult(p0: LocationResult?) {
            value = p0?.lastLocation
        }
    }
}