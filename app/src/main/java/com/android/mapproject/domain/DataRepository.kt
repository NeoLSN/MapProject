package com.android.mapproject.domain

import com.android.mapproject.domain.model.ParkingPlace
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