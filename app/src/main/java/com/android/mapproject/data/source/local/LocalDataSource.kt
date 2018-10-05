package com.android.mapproject.data.source.local

import com.android.mapproject.data.source.remote.ParkingDataRecord
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by JasonYang.
 */
class LocalDataSource(
        private val placesDao: ParkingPlacesDao,
        private val mapper: DataMapper
) {

    fun insert(place: ParkingDataRecord): Completable =
            Completable.fromAction { placesDao.insert(mapper.toDb(place)) }

    fun insertAll(places: List<ParkingDataRecord>): Completable =
            Completable.fromAction {
                placesDao.insertAll(places.map { mapper.toDb(it) })
            }

    fun placeById(id: String): Flowable<ParkingDataRecord> =
            placesDao.placeById(id).map { mapper.fromDb(it) }

    fun allPlaces(): Flowable<List<ParkingDataRecord>> =
            placesDao.allPlaces().map { it.map { p -> mapper.fromDb(p) } }
}