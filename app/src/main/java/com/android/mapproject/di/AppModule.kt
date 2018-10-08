package com.android.mapproject.di

import android.app.Application
import com.android.mapproject.domain.*
import com.android.mapproject.presentation.MainActivity
import com.android.mapproject.presentation.placedetail.PlaceDetailFragment
import com.android.mapproject.presentation.placedetail.PlaceDetailViewModelFactory
import com.android.mapproject.presentation.places.ParkingPlacesFragment
import com.android.mapproject.presentation.places.ParkingPlacesViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

/**
 * Created by JasonYang.
 */
@Module
abstract class AppModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeParkingPlacesFragment(): ParkingPlacesFragment

    @ContributesAndroidInjector
    abstract fun contributePlaceDetailFragment(): PlaceDetailFragment

    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideContext(application: Application) = application.applicationContext

        @Singleton
        @JvmStatic
        @Provides
        fun provideParkingPlacesViewModelFactory(
                refresh: RefreshParkingPlacesUseCase,
                getPlaces: GetParkingPlacesUseCase,
                filter: FilterParkingPlacesUseCase
        ) = ParkingPlacesViewModelFactory(refresh, getPlaces, filter)

        @Singleton
        @JvmStatic
        @Provides
        fun providePlaceDetailViewModelFactory(
                getPlace: GetParkingPlaceUseCase,
                getLocation: GetLocationUseCase,
                calculateRoute: CalculateRouteUseCase
        ) = PlaceDetailViewModelFactory(getPlace, getLocation, calculateRoute)
    }
}