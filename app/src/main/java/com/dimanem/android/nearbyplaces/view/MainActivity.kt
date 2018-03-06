package com.dimanem.android.nearbyplaces.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.dimanem.android.nearbyplaces.R
import com.dimanem.android.nearbyplaces.entities.Place
import com.dimanem.android.nearbyplaces.entities.Resource
import com.dimanem.android.nearbyplaces.entities.Status
import com.dimanem.android.nearbyplaces.viewmodel.NearbyPlacesViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: NearbyPlacesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NearbyPlacesViewModel::class.java)
        viewModel?.places?.observe(this, Observer<Resource<List<Place>>> { resource ->
            if (resource != null && resource.status == Status.SUCCESS) {
                Timber.e("Fetched items: " + resource.data?.toString())
            }
        })
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }
}
