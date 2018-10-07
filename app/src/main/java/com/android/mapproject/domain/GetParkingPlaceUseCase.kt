package com.android.mapproject.domain

import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class GetParkingPlaceUseCase @Inject constructor(private val dataRepo: DataRepository) {
    fun getPlace(id: String) = dataRepo.getPlace(id)
}