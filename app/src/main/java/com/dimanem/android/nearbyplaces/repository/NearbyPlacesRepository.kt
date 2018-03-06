package com.dimanem.android.nearbyplaces.repository

import android.arch.lifecycle.LiveData
import android.location.Location
import com.dimanem.android.nearbyplaces.BuildConfig
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.repository.api.ApiResponse
import com.dimanem.android.nearbyplaces.repository.api.googleplaces.GooglePlacesApi
import com.dimanem.android.nearbyplaces.repository.api.googleplaces.GooglePlacesApiResponse
import com.dimanem.android.nearbyplaces.repository.db.NearbyPlacesDB
import com.dimanem.android.nearbyplaces.repository.db.NearbyPlacesDao
import com.dimanem.android.nearbyplaces.repository.util.AppExecutors
import com.dimanem.android.nearbyplaces.repository.util.NetworkBoundResource
import com.dimanem.android.nearbyplaces.entities.Resource
import javax.inject.Inject

/**
 * Created by dimanemets on 05/03/2018.
 */
class NearbyPlacesRepository() {

    lateinit var appExecutors: AppExecutors
    lateinit var db: NearbyPlacesDB
    lateinit var api: GooglePlacesApi
    lateinit var dao: NearbyPlacesDao

    @Inject
    constructor(appExecutors: AppExecutors, db: NearbyPlacesDB,
                api: GooglePlacesApi, dao: NearbyPlacesDao) : this() {
        this.appExecutors = appExecutors
        this.db = db
        this.api = api
        this.dao = dao
    }

    fun getNearbyPlaces(location: Location, radius: Int, isOpenNow: Boolean): LiveData<Resource<List<Place>>> {
        return object : NetworkBoundResource<List<Place>, GooglePlacesApiResponse>(appExecutors) {

            override fun shouldFetch(data: List<Place>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun saveCallResult(item: GooglePlacesApiResponse?) {
                if (item?.results != null) {
                    dao.insert(item.results.map { result ->
                        Place(location.latitude, location.longitude,
                                result.geometry?.location?.lat,
                                result.geometry?.location?.lng,
                                result.icon,
                                result.rating,
                                result.vicinity) })
                }
            }

            override fun loadFromDb(): LiveData<List<Place>> {
                return dao.getNearbyPlaces(location.latitude, location.longitude)
            }

            override fun createCall(): LiveData<ApiResponse<GooglePlacesApiResponse>> {
                return api.getNearbyPlaces(
                        BuildConfig.API_KEY,
                        "${location.latitude},${location.longitude}",
                        radius,
                        isOpenNow)
            }
        }.asLiveData()
    }
}
