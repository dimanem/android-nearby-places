package com.dimanem.android.nearbyplaces.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.dimanem.android.nearbyplaces.entities.Place

/**
 * Created by dimanemets on 05/03/2018.
 */
@Database(entities = arrayOf(Place::class), version = 1)
abstract class NearbyPlacesDB : RoomDatabase() {
    abstract fun placeDao(): NearbyPlacesDao
}
