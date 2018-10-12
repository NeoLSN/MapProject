package com.android.mapproject.presentation.placedetail

import android.util.Log
import androidx.databinding.ObservableField
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

    val place = ObservableField<ParkingPlace>()
    val destination = MutableLiveData<Result<LatLng>>()
    val locations = MutableLiveData<Result<Pair<LatLng, LatLng>>>()
    val route = MutableLiveData<Result<List<LatLng>>>()

    fun getParkingPlace(id: String) {
        getPlace.getPlace(id)
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    when (it) {
                        is Result.Success -> {
                            it.data?.apply {
                                if (place.get() == this) return@apply
                                place.set(this)
                                val twdX = tw97x?.toDouble() ?: 0.0
                                val twdY = tw97y?.toDouble() ?: 0.0
                                val latLng = transformer.twd97ToWgs84(twdX, twdY)
                                destination.postValue(Result.success(latLng))
                            }
                        }
                        is Result.Failure -> destination.postValue(Result.failure(it.errorMessage, it.e))
                        is Result.InProgress -> destination.postValue(Result.inProgress())
                    }
                }
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
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    when (it) {
                        is Result.Success -> {
                            val origin = it.data
                            val v = destination.value
                            if (v is Result.Success) {
                                locations.postValue(Result.success(Pair(origin, v.data)))
                                calculateRoute(origin, v.data)
                            }
                        }
                        is Result.Failure -> locations.postValue(Result.failure(it.errorMessage, it.e))
                        is Result.InProgress -> locations.postValue(Result.inProgress())
                    }
                }
                .addTo(disposables)
    }

    private fun calculateRoute(origin: LatLng, destination: LatLng) {
        calculateRoute.calculateRoute(origin, destination)
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    when (it) {
                        is Result.Success -> route.postValue(Result.success(it.data))
                        is Result.Failure -> route.postValue(Result.failure(it.errorMessage, it.e))
                        is Result.InProgress -> route.postValue(Result.inProgress())
                    }
                }
                .addTo(disposables)
    }
}