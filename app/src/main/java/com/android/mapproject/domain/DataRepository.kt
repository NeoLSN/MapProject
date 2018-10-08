package com.android.mapproject.domain

import com.android.mapproject.domain.model.ParkingPlace
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Created by JasonYang.
 */
interface DataRepository {

    fun allPlaces(): Flowable<List<ParkingPlace>>

    fun filter(term: String): Flowable<List<ParkingPlace>>

    fun getPlace(id: String): Flowable<ParkingPlace>

    fun refreshPlaces(): Completable

    fun getCurrentLocation(): Observable<LatLng>

    fun calculateRoute(origin: LatLng, destination: LatLng): Flowable<List<LatLng>>
}