package com.android.mapproject.data

import com.android.mapproject.data.source.local.LocalDataSource
import com.android.mapproject.data.source.map.MapDataSource
import com.android.mapproject.data.source.remote.RemoteDataSource
import com.android.mapproject.domain.DataRepository
import com.google.android.gms.maps.model.LatLng

/**
 * Created by JasonYang.
 */
class DataRepositoryImpl(
        private val remote: RemoteDataSource,
        private val local: LocalDataSource,
        private val map: MapDataSource
) : DataRepository {

    override fun allPlaces() = local.allPlaces()

    override fun filter(term: String) = local.fliter(term)

    override fun getPlace(id: String) = local.placeById(id)

    override fun refreshPlaces() =
            remote.allPlaces()
                    .flatMapCompletable { places ->
                        local.deleteAll().andThen(local.insertAll(places))
                    }

    override fun getCurrentLocation() = map.getCurrentLocation()

    override fun calculateRoute(origin: LatLng, destination: LatLng) =
            map.calculateRoute(origin, destination)

}