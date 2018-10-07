package com.android.mapproject.data

import com.android.mapproject.data.source.local.LocalDataSource
import com.android.mapproject.data.source.remote.RemoteDataSource
import com.android.mapproject.domain.DataRepository

/**
 * Created by JasonYang.
 */
class DataRepositoryImpl(
        private val remote: RemoteDataSource,
        private val local: LocalDataSource
) : DataRepository {

    override fun allPlaces() = local.allPlaces()

    override fun filter(term: String) = local.fliter(term)

    override fun getPlace(id: String) = local.placeById(id)

    override fun refreshPlaces() =
            remote.allPlaces()
                    .flatMapCompletable { places -> local.insertAll(places) }
}