package com.android.mapproject.domain.usecase

import com.android.mapproject.domain.DataRepository
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class FilterParkingPlacesUseCase @Inject constructor(private val dataRepo: DataRepository) {
    fun filter(term: String) = dataRepo.filter(term)
}