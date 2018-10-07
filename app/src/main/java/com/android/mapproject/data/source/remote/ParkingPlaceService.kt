package com.android.mapproject.data.source.remote

import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * Created by JasonYang.
 */
interface ParkingPlaceService {

    companion object {
        const val HTTPS_API_URL = "http://data.ntpc.gov.tw/"
    }

    @GET("/api/v1/rest/datastore/382000000A-000225-002")
    fun getParkingPlaces(): Flowable<ParkingDataResponse>
}