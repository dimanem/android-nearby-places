package com.dimanem.android.nearbyplaces

import com.dimanem.android.nearbyplaces.view.list.NearbyPlacesListFragment
import com.dimanem.android.nearbyplaces.view.map.NearbyPlacesMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by dimanemets on 10/02/2018.
 */
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun nearbyListFragment(): NearbyPlacesListFragment

    @ContributesAndroidInjector
    abstract fun nearbyMapFragment(): NearbyPlacesMapFragment
}
