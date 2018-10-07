package com.android.mapproject.presentation.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.mapproject.domain.GetParkingPlaceUseCase

/**
 * Created by JasonYang.
 */
class PlaceDetailViewModelFactory(
        private val getPlace: GetParkingPlaceUseCase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceDetailViewModel::class.java)) {
            return PlaceDetailViewModel(getPlace) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}