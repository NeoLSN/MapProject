package com.android.mapproject.data.source.remote

import com.android.mapproject.domain.ParkingPlace
import io.reactivex.Single

/**
 * Created by JasonYang.
 */
class RemoteDataSource(private val service: ParkingPlaceService) {

    fun allPlaces(): Single<List<ParkingPlace>> {
        return service.getParkingPlaces()
                .map { it.result?.records ?: ArrayList() }
    }
}