package com.dimanem.android.nearbyplaces.view.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dimanemets on 06/03/2018.
 */
@Singleton
class LocationUtil {

    var application: Application

    @Inject
    constructor(application: Application) {
        this.application = application
    }

    fun isPermissionGranted() : Boolean {
        return (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
}
