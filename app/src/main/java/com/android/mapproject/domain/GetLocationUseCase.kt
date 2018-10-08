package com.android.mapproject.domain

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class GetLocationUseCase @Inject constructor(private val rxLocation: RxLocation) {

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
}