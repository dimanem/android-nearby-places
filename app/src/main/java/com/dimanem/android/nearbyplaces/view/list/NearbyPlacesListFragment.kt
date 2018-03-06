package com.dimanem.android.nearbyplaces.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimanem.android.nearbyplaces.R
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.view.NearbyPlacesFragment
import timber.log.Timber

/**
 * Created by dimanemets on 05/03/2018.
 */
class NearbyPlacesListFragment : NearbyPlacesFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_nearby_list, container, false)
    }

    override fun showLoading() {
        Timber.d("Loading...")
    }

    override fun showPlaces(places: List<Place>?) {
        Timber.d("Showing ${places?.size} places")
    }

    override fun showError(error: String?) {
        Timber.e("Failed to load with error: $error")
    }
}
