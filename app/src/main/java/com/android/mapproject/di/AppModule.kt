package com.android.mapproject.di

import android.app.Application
import com.android.mapproject.domain.GetParkingPlaceUseCase
import com.android.mapproject.domain.GetParkingPlacesUseCase
import com.android.mapproject.domain.RefreshParkingPlacesUseCase
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
                getPlaces: GetParkingPlacesUseCase
        ) = ParkingPlacesViewModelFactory(refresh, getPlaces)

        @Singleton
        @JvmStatic
        @Provides
        fun providePlaceDetailViewModelFactory(
                getPlace: GetParkingPlaceUseCase
        ) = PlaceDetailViewModelFactory(getPlace)
    }
}