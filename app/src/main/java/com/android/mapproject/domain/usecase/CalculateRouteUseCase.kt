package com.android.mapproject.domain.usecase

import com.android.mapproject.data.DataRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class CalculateRouteUseCase @Inject constructor(private val dataRepo: DataRepository) {

    fun calculateRoute(origin: LatLng, destination: LatLng) =
            dataRepo.calculateRoute(origin, destination)
}