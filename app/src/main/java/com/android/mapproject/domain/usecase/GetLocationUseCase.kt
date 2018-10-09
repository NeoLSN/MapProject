package com.android.mapproject.domain.usecase

import com.android.mapproject.data.DataRepository
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class GetLocationUseCase @Inject constructor(private val dataRepo: DataRepository) {

    fun getCurrentLocation(): Observable<LatLng> = dataRepo.getCurrentLocation()
}