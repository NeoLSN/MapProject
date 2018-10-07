package com.android.mapproject.domain

import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class FilterParkingPlacesUseCase @Inject constructor(private val dataRepo: DataRepository) {
    fun filter(term: String) = dataRepo.filter(term)
}