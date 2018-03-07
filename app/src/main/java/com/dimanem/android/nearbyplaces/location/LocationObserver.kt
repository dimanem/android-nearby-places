package com.dimanem.android.nearbyplaces.view.location

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest


/**
 * Created by dimanemets on 06/03/2018.
 */
class LocationObserver : LifecycleObserver,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    interface Callback {
        fun onLocationChanged(location: Location)
    }

    private val application: Application
    private val lifecycle: Lifecycle
    private val callback: Callback
    private val locationUtil: LocationUtil

    private val googleApiClient: GoogleApiClient
    private var enabled = false

    constructor(application: Application, lifecycle: Lifecycle, locationUtil: LocationUtil, callback: Callback) {
        this.application = application
        this.lifecycle = lifecycle
        this.locationUtil = locationUtil
        this.callback = callback
        this.googleApiClient = GoogleApiClient.Builder(application)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        if (enabled) {
            connectIfNeeded()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disconnectIfNeeded()
    }

    fun enable() {
        enabled = true
        if (isLifecycleActive()) {
            connectIfNeeded()
//            fetchLastKnownLocation()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (locationUtil.isPermissionGranted()) {
//            fetchLastKnownLocation()
            if (isLifecycleActive()) {
                registerForLocationUpdates()
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onLocationChanged(p0: Location?) {
        if (isLifecycleActive() && p0 != null) {
            callback.onLocationChanged(p0)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLastKnownLocation() {
        val lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)

        if (lastLocation != null) {
            if (isLifecycleActive()) {
                callback.onLocationChanged(lastLocation)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun registerForLocationUpdates() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    private fun isLifecycleActive() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

    private fun connectIfNeeded() {
        if (!googleApiClient.isConnected) {
            googleApiClient.connect()
        }
    }

    private fun disconnectIfNeeded() {
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
    }
}
