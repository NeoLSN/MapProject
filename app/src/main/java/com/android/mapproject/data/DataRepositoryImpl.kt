package com.android.mapproject.data

import com.android.mapproject.data.source.local.LocalDataSource
import com.android.mapproject.data.source.map.MapDataSource
import com.android.mapproject.data.source.remote.RemoteDataSource
import com.android.mapproject.util.rx.SchedulerProvider
import com.google.android.gms.maps.model.LatLng

/**
 * Created by JasonYang.
 */
class DataRepositoryImpl(
        private val remote: RemoteDataSource,
        private val local: LocalDataSource,
        private val map: MapDataSource,
        private val schedulerProvider: SchedulerProvider
) : DataRepository {

    override fun allPlaces() = local.allPlaces().subscribeOn(schedulerProvider.io())

    override fun filter(term: String) = local.fliter(term).subscribeOn(schedulerProvider.io())

    override fun getPlace(id: String) = local.placeById(id).subscribeOn(schedulerProvider.io())

    override fun refreshPlaces() =
            remote.allPlaces()
                    .flatMapCompletable { places ->
                        local.deleteAll().andThen(local.insertAll(places))
                    }
                    .subscribeOn(schedulerProvider.io())

    override fun getCurrentLocation() = map.getCurrentLocation().subscribeOn(schedulerProvider.io())

    override fun calculateRoute(origin: LatLng, destination: LatLng) =
            map.calculateRoute(origin, destination)
                    .subscribeOn(schedulerProvider.computation())

}