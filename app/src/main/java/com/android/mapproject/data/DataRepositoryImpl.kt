package com.android.mapproject.data

import com.android.mapproject.data.source.local.LocalDataSource
import com.android.mapproject.data.source.remote.RemoteDataSource
import com.android.mapproject.domain.DataRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * Created by JasonYang.
 */
class DataRepositoryImpl(
        private val remote: RemoteDataSource,
        private val local: LocalDataSource
) : DataRepository {

    override fun allPlaces() = local.allPlaces()

    override fun getPlace(id: String) = local.placeById(id)

    override fun refreshPlaces() = Completable.fromSingle(
            remote.allPlaces()
                    .subscribeOn(Schedulers.computation())
                    .doOnSuccess { places -> local.insertAll(places) }
    )
}