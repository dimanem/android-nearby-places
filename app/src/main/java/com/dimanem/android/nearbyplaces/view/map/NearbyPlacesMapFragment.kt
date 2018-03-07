package com.dimanem.android.nearbyplaces.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimanem.android.nearbyplaces.R
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.view.NearbyPlacesFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber


/**
 * Created by dimanemets on 05/03/2018.
 */
class NearbyPlacesMapFragment : NearbyPlacesFragment() {

    var mapView: MapView? = null
    private var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_nearby_map, container, false)
        mapView = rootView?.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView?.getMapAsync({ map ->
            setMap(map)
        })

        return rootView
    }

    override fun showLoading() {
    }

    override fun showPlaces(places: List<Place>?) {
        super.showPlaces(places)
        if (this.map == null) {
            mapView?.getMapAsync({ map ->
                setMap(map)
                showPlacesOnMap(map, places)
            })
        }
    }

    private fun setMap(map: GoogleMap) {
        map.uiSettings.isZoomGesturesEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        this.map = map
    }

    private fun showPlacesOnMap(map: GoogleMap, places: List<Place>?) {
        if (places != null && !places.isEmpty() && map != null) {
            Timber.d("Showing ${places?.size} places on map")
            places.forEach { place ->
                val lat = place.lat
                val lon = place.lon
                if (lat != null && lon != null) {
                    val latLon = LatLng(lat, lon)
                    val markerOptions = MarkerOptions().position(latLon)

                    val name = place.name
                    if (name != null) {
                        markerOptions.title(name)
                    }
                    map?.addMarker(markerOptions)
                }
            }

            val baseLat = places[0].lat
            val baseLon = places[0].lon
            if (baseLat != null && baseLon != null) {
                val baseLatLon = LatLng(baseLat, baseLon)
                val cameraPosition = CameraPosition.Builder().zoom(12f).target(baseLatLon).build()
                map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }
}
