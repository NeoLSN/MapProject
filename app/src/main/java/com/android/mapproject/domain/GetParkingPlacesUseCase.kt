package com.android.mapproject.domain

import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class GetParkingPlacesUseCase @Inject constructor(private val dataRepo: DataRepository) {
    fun allPlaces() = dataRepo.allPlaces()
}