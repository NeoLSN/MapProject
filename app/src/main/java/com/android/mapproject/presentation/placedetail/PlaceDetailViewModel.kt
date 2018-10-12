package com.android.mapproject.presentation.placedetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.data.source.map.CoordinateTransformer
import com.android.mapproject.domain.model.ParkingPlace
import com.android.mapproject.domain.usecase.CalculateRouteUseCase
import com.android.mapproject.domain.usecase.GetLocationUseCase
import com.android.mapproject.domain.usecase.GetParkingPlaceUseCase
import com.android.mapproject.presentation.BaseViewModel
import com.android.mapproject.presentation.common.Result
import com.android.mapproject.presentation.common.toResult
import com.android.mapproject.util.rx.SchedulerProvider
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class PlaceDetailViewModel @Inject constructor(
        private val getPlace: GetParkingPlaceUseCase,
        private val getLocation: GetLocationUseCase,
        private val calculateRoute: CalculateRouteUseCase,
        private val transformer: CoordinateTransformer,
        private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val place = MutableLiveData<Result<ParkingPlace>>()
    val destination = MutableLiveData<LatLng>()
    val locations = MutableLiveData<Result<Pair<LatLng, LatLng>>>()
    val route = MutableLiveData<Result<List<LatLng>>>()

    fun getParkingPlace(id: String) {
        getPlace.getPlace(id)
                .firstOrError()
                .doOnSuccess {
                    it?.apply {
                        val twdX = tw97x?.toDouble() ?: 0.0
                        val twdY = tw97y?.toDouble() ?: 0.0
                        val latLng = transformer.twd97ToWgs84(twdX, twdY)
                        if (destination.value == latLng) return@apply
                        destination.postValue(latLng)
                    }
                }
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe { place.postValue(it) }
                .addTo(disposables)
    }

    fun requestLocation(request: Observable<Boolean>) {
        request
                .subscribeBy(
                        onNext = { granted -> if (granted) getCurrentLocation() },
                        onError = { e -> Log.w("PlaceDetailViewModel", "Location wrong $e") }
                )
                .addTo(disposables)
    }

    private fun getCurrentLocation() {
        getLocation.getCurrentLocation()
                .firstOrError()
                .map { Pair(it, destination.value!!) }
                .doOnSuccess { calculateRoute(it.first, it.second) }
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe { locations.postValue(it) }
                .addTo(disposables)
    }

    private fun calculateRoute(origin: LatLng, destination: LatLng) {
        calculateRoute.calculateRoute(origin, destination)
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe { route.postValue(it) }
                .addTo(disposables)
    }
}