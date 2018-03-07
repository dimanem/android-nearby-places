package com.dimanem.android.nearbyplaces.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimanem.android.nba.rssreader.di.Injectable
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.entities.Resource
import com.dimanem.android.nearbyplaces.entities.Status
import com.dimanem.android.nearbyplaces.viewmodel.NearbyPlacesViewModel
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by dimanemets on 06/03/2018.
 */
abstract class NearbyPlacesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: NearbyPlacesViewModel? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = activity
        viewModel = ViewModelProviders.of(scope, viewModelFactory).get(NearbyPlacesViewModel::class.java)

        viewModel?.nearByPlaces?.observe(this, Observer<Resource<List<Place>>> { resource ->
            when {
                resource?.status == Status.LOADING -> {
                    showLoading()
                }
                resource?.status == Status.SUCCESS -> {
                    showPlaces(resource.data)
                }
                else -> {
                    showError(resource?.message)
                }
            }
        })
    }

    override abstract fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    abstract fun showPlaces(places: List<Place>?)

    open fun showLoading() {
        showSnackbar("Loading...")
    }

    open fun showError(error: String?) {
        val errorMsg = "Failed to load locations with error: $error"
        Timber.e(errorMsg)
        showSnackbar(errorMsg)
    }

    fun showSnackbar(text: String) {
        Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show()
    }
}
