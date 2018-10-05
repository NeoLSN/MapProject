package com.android.mapproject.domain

import com.android.mapproject.data.source.remote.ParkingDataRecord
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by JasonYang.
 */
interface DataRepository {

    fun allPlaces(): Flowable<List<ParkingDataRecord>>

    fun getPlace(id: String): Flowable<ParkingDataRecord>

    fun refreshPlaces(): Completable
}