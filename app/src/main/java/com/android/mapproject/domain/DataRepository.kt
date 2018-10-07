package com.android.mapproject.domain

import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by JasonYang.
 */
interface DataRepository {

    fun allPlaces(): Flowable<List<ParkingPlace>>

    fun filter(term: String): Flowable<List<ParkingPlace>>

    fun getPlace(id: String): Flowable<ParkingPlace>

    fun refreshPlaces(): Completable
}