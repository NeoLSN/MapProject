package com.android.mapproject.data.source.remote

import com.android.mapproject.domain.ParkingPlace
import io.reactivex.Flowable

/**
 * Created by JasonYang.
 */
class RemoteDataSource(private val service: ParkingPlaceService) {

    fun allPlaces(): Flowable<List<ParkingPlace>> {
        return service.getParkingPlaces()
                .map { it.result?.records ?: ArrayList() }
    }
}