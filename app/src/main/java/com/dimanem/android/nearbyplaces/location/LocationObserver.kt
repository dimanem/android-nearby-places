package com.dimanem.android.nearbyplaces.view.location

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.location.Location
import com.google.android.gms.location.*
import javax.inject.Inject


/**
 * Created by dimanemets on 06/03/2018.
 */
class LocationObserver : LifecycleObserver, LocationCallback {

    interface Callback {
        fun onLocationChanged(location: Location)
    }

    private val application: Application
    private val locationUtil: LocationUtil

    private val fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequest: LocationRequest

    private var enabled = false
    private var isRequestingUpdates = false

    var callback: Callback? = null

    @Inject
    constructor(application: Application, locationUtil: LocationUtil, fusedLocationProviderClient: FusedLocationProviderClient, locationRequest: LocationRequest) {
        this.application = application
        this.locationUtil = locationUtil
        this.fusedLocationProviderClient = fusedLocationProviderClient
        this.locationRequest = locationRequest
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled && locationUtil.isPermissionGranted()) {
            requestLocationUpdates()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        fusedLocationProviderClient.removeLocationUpdates(this)
        isRequestingUpdates = false
    }

    fun enable() {
        enabled = true
        if (locationUtil.isPermissionGranted()) {
            requestLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        if (!isRequestingUpdates) {
            isRequestingUpdates = true
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, this, null /* Looper */)
        }
    }

    override fun onLocationResult(p0: LocationResult?) {
        if (p0 != null) {
            callback?.onLocationChanged(p0?.lastLocation)
        }
    }

    override fun onLocationAvailability(p0: LocationAvailability?) {
        super.onLocationAvailability(p0)
    }
}
