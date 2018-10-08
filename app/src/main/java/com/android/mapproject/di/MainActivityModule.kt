package com.android.mapproject.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.android.mapproject.presentation.MainActivity
import com.android.mapproject.presentation.placedetail.PlaceDetailFragment
import com.android.mapproject.presentation.placedetail.PlaceDetailViewModel
import com.android.mapproject.presentation.places.ParkingPlacesFragment
import com.android.mapproject.presentation.places.ParkingPlacesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by JasonYang.
 */
@Module interface MainActivityModule {

    @Binds
    fun provideAppCompatActivity(activity: MainActivity): AppCompatActivity

    @ContributesAndroidInjector
    fun contributeParkingPlacesFragment(): ParkingPlacesFragment

    @Binds
    @IntoMap
    @ViewModelKey(ParkingPlacesViewModel::class)
    fun bindParkingPlacesViewModel(parkingPlacesViewModel: ParkingPlacesViewModel): ViewModel

    @ContributesAndroidInjector
    fun contributePlaceDetailFragment(): PlaceDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(PlaceDetailViewModel::class)
    fun bindPlaceDetailViewModel(placeDetailViewModel: PlaceDetailViewModel): ViewModel
}