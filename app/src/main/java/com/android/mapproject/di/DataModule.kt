package com.android.mapproject.di

import android.content.Context
import com.android.mapproject.data.DataRepositoryImpl
import com.android.mapproject.data.source.local.DataMapper
import com.android.mapproject.data.source.local.LocalDataSource
import com.android.mapproject.data.source.local.ParkingPlacesDao
import com.android.mapproject.data.source.local.ParkingPlacesDatabase
import com.android.mapproject.data.source.map.MapDataSource
import com.android.mapproject.data.source.remote.ParkingPlaceService
import com.android.mapproject.data.source.remote.RemoteDataSource
import com.android.mapproject.domain.DataRepository
import com.google.maps.GeoApiContext
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by JasonYang.
 */
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: ParkingPlaceService) = RemoteDataSource(service)

    @Singleton
    @Provides
    fun providePlacesDao(context: Context) =
            ParkingPlacesDatabase.getInstance(context).placesDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(placesDao: ParkingPlacesDao, mapper: DataMapper) =
            LocalDataSource(placesDao, mapper)


    @Singleton
    @Provides
    fun provideMapDataSource(geo: GeoApiContext, rxLocation: RxLocation): MapDataSource =
            MapDataSource(rxLocation, geo)

    @Singleton
    @Provides
    fun provideRepository(
            remote: RemoteDataSource,
            local: LocalDataSource,
            map: MapDataSource
    ): DataRepository = DataRepositoryImpl(remote, local, map)
}