package com.dimanem.android.nearbyplaces.viewmodel

import android.arch.lifecycle.LiveData
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

    var places: LiveData<Resource<List<Place>>> ? = null

    @Inject
    constructor(repository: NearbyPlacesRepository) {
        var location = Location("GPS")
        location.latitude = 32.109333
        location.longitude = 34.855499

        places = repository.getNearbyPlaces(location, 3000, true)
    }
}
