package com.dimanem.android.nearbyplaces.view.list

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimanem.android.nearbyplaces.R
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.view.NearbyPlacesFragment
import timber.log.Timber
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager



/**
 * Created by dimanemets on 05/03/2018.
 */
class NearbyPlacesListFragment : NearbyPlacesFragment() {

    private var recyclerView: RecyclerView? = null

    private var adapter : NearbyPlacesListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_nearby_list, container, false)

        recyclerView = view?.findViewById(R.id.recycler_view)

        adapter = NearbyPlacesListAdapter(listOf())
        recyclerView?.layoutManager = LinearLayoutManager(activity.applicationContext)
//        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter

        return view
    }

    override fun showLoading() {
        Timber.d("Loading...")
    }

    override fun showPlaces(places: List<Place>?) {
        if (places != null) {
            this.adapter?.setPlaces(places)
        }
        Timber.d("Showing ${places?.size} places")
    }

    override fun showError(error: String?) {
        Timber.e("Failed to load with error: $error")
    }
}
