package com.dimanem.android.nearbyplaces

import com.dimanem.android.nearbyplaces.view.MainActivity
import com.dimanem.android.nearbyplaces.viewmodel.di.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by dimanemets on 10/02/2018.
 */
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = arrayOf(FragmentBuildersModule::class))
    abstract fun contributeMainActivity(): MainActivity
}
