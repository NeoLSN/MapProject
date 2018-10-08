package com.android.mapproject.presentation.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.mapproject.domain.usecase.CalculateRouteUseCase
import com.android.mapproject.domain.usecase.GetLocationUseCase
import com.android.mapproject.domain.usecase.GetParkingPlaceUseCase

/**
 * Created by JasonYang.
 */
class PlaceDetailViewModelFactory(
        private val getPlace: GetParkingPlaceUseCase,
        private val getLocation: GetLocationUseCase,
        private val calculateRoute: CalculateRouteUseCase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceDetailViewModel::class.java)) {
            return PlaceDetailViewModel(getPlace, getLocation, calculateRoute) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}