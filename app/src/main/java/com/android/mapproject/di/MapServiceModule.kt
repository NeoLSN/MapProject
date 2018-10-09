package com.android.mapproject.di

import android.content.Context
import com.android.mapproject.R
import com.android.mapproject.data.source.map.CoordinateTransformer
import com.google.maps.GeoApiContext
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by JasonYang.
 */
@Module
class MapServiceModule {

    @Singleton
    @Provides
    fun provideRxLocation(context: Context) = RxLocation(context)

    @Singleton
    @Provides
    fun provideGeoApiContext(context: Context): GeoApiContext {
        val geoApiContext = GeoApiContext.Builder()
        return geoApiContext
                .apiKey(context.getString(R.string.google_directions_api_key))
                .queryRateLimit(3)
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    fun provideCoordinateTransformer() = CoordinateTransformer()
}