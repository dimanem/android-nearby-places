package com.dimanem.android.nba.rssreader.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.dimanem.android.nearbyplaces.NearbyPlacesApp
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Created by dimanemets on 09/02/2018.
 */
class AppInjector {
    private constructor()

    companion object {
        fun init(app: NearbyPlacesApp) {
            DaggerAppComponent.builder().application(app)
                    .build().inject(app)
            app
                    .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                            handleActivity(activity)
                        }

                        override fun onActivityStarted(activity: Activity) {

                        }

                        override fun onActivityResumed(activity: Activity) {

                        }

                        override fun onActivityPaused(activity: Activity) {

                        }

                        override fun onActivityStopped(activity: Activity) {

                        }

                        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

                        }

                        override fun onActivityDestroyed(activity: Activity) {

                        }
                    })
        }

        private fun handleActivity(activity: Activity) {
            // Inject activity
            if (activity is HasSupportFragmentInjector) {
                AndroidInjection.inject(activity)
            }
            // Inject fragments
            (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?,
                                                       savedInstanceState: Bundle?) {
                            if (f is Injectable) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true)
        }
    }
}
