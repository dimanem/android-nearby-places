package com.dimanem.android.nearbyplaces.repository.api.googleplaces

import android.arch.lifecycle.LiveData
import com.dimanem.android.nearbyplaces.repository.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by dimanemets on 05/03/2018.
 */
interface GooglePlacesApi {

    // Location String format: latitude,longitude
    @GET("json")
    fun getNearbyPlaces(@Query("key") key: String,
                        @Query("location") location: String,
                        @Query("radius") radius: Int,
                        @Query("opennow") isOpenNow: Boolean):
            LiveData<ApiResponse<GooglePlacesApiResponse>>
}
