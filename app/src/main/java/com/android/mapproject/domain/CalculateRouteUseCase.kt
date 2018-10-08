package com.android.mapproject.domain

import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class CalculateRouteUseCase @Inject constructor(private val geo: GeoApiContext) {

    fun calculateRoute(origin: LatLng, destination: LatLng): DirectionsResult {
        return DirectionsApi.newRequest(geo)
                .origin(origin)
                .destination(destination)
                .await()
    }
}