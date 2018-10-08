package com.android.mapproject.domain.usecase

import com.android.mapproject.domain.DataRepository
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class RefreshParkingPlacesUseCase @Inject constructor(private val dataRepo: DataRepository) {
    fun refreshParkingPlaces() = dataRepo.refreshPlaces()
}