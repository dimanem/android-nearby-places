package com.dimanem.android.nearbyplaces.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.repository.NearbyPlacesRepository
import com.dimanem.android.nearbyplaces.entities.Resource
import javax.inject.Inject

/**
 * Created by dimanemets on 06/03/2018.
 */
class NearbyPlacesViewModel : ViewModel {

    var currentLocation: MutableLiveData<Location> = MutableLiveData()
    var nearByPlaces: LiveData<Resource<List<Place>>> ? = null

    @Inject
    constructor(repository: NearbyPlacesRepository) {
        nearByPlaces = Transformations.switchMap(currentLocation) {
            if (it != null) {
                // TODO radius and isOpenNow shouldn't be hardcoded (use shared prefs)
                repository.getNearbyPlaces(it, 2000, false)
            } else {
                MutableLiveData()
            }
        }
    }

    fun setCurrentLocation(location: Location) {
        currentLocation.value = location
    }
}
