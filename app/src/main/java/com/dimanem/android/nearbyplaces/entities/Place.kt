package com.dimanem.android.nearbyplaces.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by dimanemets on 05/03/2018.
 */
@Entity(tableName = "places")
class Place() {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "base_lat")
    var baseLat: Double = 0.0
    @ColumnInfo(name = "base_lon")
    var baseLon: Double = 0.0

    @ColumnInfo(name = "lat")
    var lat: Double? = 0.0
    @ColumnInfo(name = "lon")
    var lon: Double? = 0.0
    var icon: String? = null
    var rating: Double? = null
    var address: String? = null

    @Ignore
    constructor(baseLat: Double, baseLon: Double, lat: Double?, lon: Double?, icon: String?, rating: Double?, address: String?): this() {
        this.baseLat = baseLat
        this.baseLon = baseLon
        this.lat = lat
        this.lon = lon
        this.icon = icon
        this.rating = rating
        this.address = address
    }
}
