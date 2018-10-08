package com.android.mapproject.data.source.map

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.patloew.rxlocation.RxLocation
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by JasonYang.
 */
class MapDataSource(
        private val rxLocation: RxLocation,
        private val geo: GeoApiContext
) {
    fun getCurrentLocation(): Observable<LatLng> {
        rxLocation.setDefaultTimeout(15, TimeUnit.SECONDS)
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(1000)
                .setInterval(5000)

        return rxLocation.settings()
                .checkAndHandleResolution(locationRequest)
                .flatMapObservable { getLatLngObservable(rxLocation, locationRequest, it) }
                .map { location -> LatLng(location.latitude, location.longitude) }
    }

    @SuppressLint("MissingPermission")
    private fun getLatLngObservable(rxLocation: RxLocation, locationRequest: LocationRequest,
                                    success: Boolean): Observable<Location> {
        return if (success) {
            rxLocation.location().updates(locationRequest)
        } else {
            rxLocation.location().lastLocation().toObservable()
        }
    }

    fun calculateRoute(origin: LatLng, destination: LatLng): Flowable<List<LatLng>> {
        val o = com.google.maps.model.LatLng(origin.latitude, origin.longitude)
        val d = com.google.maps.model.LatLng(destination.latitude, destination.longitude)
        val request = DirectionsApi.newRequest(geo)
                .origin(o)
                .destination(d)

        return Flowable.just(request)
                .map { it.await() }
                .map { result -> PolyUtil.decode(result.routes[0].overviewPolyline.encodedPath) }

    }
}