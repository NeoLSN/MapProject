package com.android.mapproject.presentation.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.mapproject.domain.FilterParkingPlacesUseCase
import com.android.mapproject.domain.GetParkingPlacesUseCase
import com.android.mapproject.domain.RefreshParkingPlacesUseCase

/**
 * Created by JasonYang.
 */
class ParkingPlacesViewModelFactory(
        private val refresh: RefreshParkingPlacesUseCase,
        private val getPlaces: GetParkingPlacesUseCase,
        private val filter: FilterParkingPlacesUseCase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkingPlacesViewModel::class.java)) {
            return ParkingPlacesViewModel(refresh, getPlaces, filter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}