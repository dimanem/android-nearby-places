package com.dimanem.android.nearbyplaces.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.entities.Resource
import com.dimanem.android.nearbyplaces.location.LocationLiveData
import com.dimanem.android.nearbyplaces.repository.NearbyPlacesRepository
import javax.inject.Inject

/**
 * Created by dimanemets on 06/03/2018.
 */
class NearbyPlacesViewModel : ViewModel {

    var nearByPlaces: LiveData<Resource<List<Place>>> ? = null


    @Inject
    constructor(repository: NearbyPlacesRepository, locationData: LocationLiveData) {
        nearByPlaces = Transformations.switchMap(locationData) {
            if (it != null) {
                // TODO radius and isOpenNow shouldn't be hardcoded (use shared prefs)
                repository.getNearbyPlaces(it, 2000, false)
            } else {
                MutableLiveData()
            }
        }
    }
}
