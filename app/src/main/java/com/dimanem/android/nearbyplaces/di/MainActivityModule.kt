package com.dimanem.android.nba.rssreader.di

import com.dimanem.android.nearbyplaces.view.MainActivity
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
