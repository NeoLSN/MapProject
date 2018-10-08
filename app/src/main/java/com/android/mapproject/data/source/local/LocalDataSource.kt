package com.android.mapproject.data.source.local

import com.android.mapproject.domain.model.ParkingPlace
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by JasonYang.
 */
class LocalDataSource(
        private val placesDao: ParkingPlacesDao,
        private val mapper: DataMapper
) {

    fun insert(place: ParkingPlace): Completable =
            Completable.fromAction { placesDao.insert(mapper.toDb(place)) }

    fun insertAll(places: List<ParkingPlace>): Completable =
            Completable.fromAction {
                placesDao.insertAll(places.map { mapper.toDb(it) })
            }

    fun placeById(id: String): Flowable<ParkingPlace> =
            placesDao.placeById(id).map { mapper.fromDb(it) }

    fun allPlaces(): Flowable<List<ParkingPlace>> =
            placesDao.allPlaces().map { it.map { p -> mapper.fromDb(p) } }

    fun fliter(term: String): Flowable<List<ParkingPlace>> =
            placesDao.filter("%$term%").map { it.map { p -> mapper.fromDb(p) } }
}