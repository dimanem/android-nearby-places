package com.dimanem.android.nba.rssreader.di

import android.app.Application
import android.arch.persistence.room.Room
import com.dimanem.android.nearbyplaces.BuildConfig
import com.dimanem.android.nearbyplaces.repository.adapters.LiveDataCallAdapterFactory
import com.dimanem.android.nearbyplaces.repository.api.googleplaces.GooglePlacesApi
import com.dimanem.android.nearbyplaces.repository.db.NearbyPlacesDao
import com.dimanem.android.nearbyplaces.repository.db.NearbyPlacesDB
import com.dimanem.android.nearbyplaces.viewmodel.di.ViewModelModule
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by dimanemets on 09/02/2018.
 */
@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

    @Singleton
    @Provides
    fun provideGooglePlacesApiService(okHttpClient: OkHttpClient): GooglePlacesApi {
        return Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create<GooglePlacesApi>(GooglePlacesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(StethoInterceptor()).build()
    }

    @Singleton
    @Provides
    fun providePlacesDb(app: Application): NearbyPlacesDB {
        return Room.databaseBuilder(app, NearbyPlacesDB::class.java, "nearby_places.db").build()
    }

    @Singleton
    @Provides
    fun providePlaceDao(db: NearbyPlacesDB): NearbyPlacesDao {
        return db.placeDao()
    }
}
