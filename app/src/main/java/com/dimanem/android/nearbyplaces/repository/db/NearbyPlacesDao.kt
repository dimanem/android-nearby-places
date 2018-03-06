package com.dimanem.android.nearbyplaces.repository.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.dimanem.android.nearbyplaces.entities.Place

/**
 * Created by dimanemets on 05/03/2018.
 */
@Dao
interface NearbyPlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<Place>)

    // Why arg0, arg1: https://stackoverflow.com/questions/44206745/android-room-each-bind-variable-in-the-query-must-have-a-matching-method
    @Query("SELECT * FROM nearByPlaces WHERE base_lat = :lat AND base_lon = :lon")
    fun getNearbyPlaces(lat: Double, lon: Double): LiveData<List<Place>>
}
