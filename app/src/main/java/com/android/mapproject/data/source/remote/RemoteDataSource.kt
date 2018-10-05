package com.android.mapproject.data.source.remote

import io.reactivex.Single

/**
 * Created by JasonYang.
 */
class RemoteDataSource(private val service: ParkingPlaceService) {

    fun allPlaces(): Single<List<ParkingDataRecord>> {
        return service.getParkingPlaces()
                .map { it.result?.records ?: ArrayList() }
    }
}