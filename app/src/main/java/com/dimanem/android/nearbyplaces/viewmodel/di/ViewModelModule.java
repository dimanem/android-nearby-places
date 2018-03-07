package com.dimanem.android.nearbyplaces.viewmodel.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.dimanem.android.nearbyplaces.viewmodel.NearbyPlacesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NearbyPlacesViewModel.class)
    abstract ViewModel bindUserViewModel(NearbyPlacesViewModel placesViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
