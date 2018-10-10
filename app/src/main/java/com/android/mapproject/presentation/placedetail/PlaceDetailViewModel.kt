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
import com.android.mapproject.util.zip
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxkotlin.addTo
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
    val destination = MutableLiveData<LatLng>()
    private val currentLocation = MutableLiveData<LatLng>()
    val locations = currentLocation.zip(destination)
    val route = MutableLiveData<List<LatLng>>()

    fun getParkingPlace(id: String) {
        getPlace.getPlace(id)
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    when (it) {
                        is Result.Success -> {
                            it.data?.apply {
                                place.set(this)
                                val twdX = tw97x?.toDouble() ?: 0.0
                                val twdY = tw97y?.toDouble() ?: 0.0
                                val latLng = transformer.twd97ToWgs84(twdX, twdY)
                                destination.postValue(latLng)
                            }
                        }
                        is Result.Failure -> Log.w("ParkingPlacesViewModel", "Get places error: ${it.e}")
                    }
                }
                .addTo(disposables)
    }

    fun getCurrentLocation() {
        getLocation.getCurrentLocation()
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    when (it) {
                        is Result.Success -> currentLocation.postValue(it.data)
                        is Result.Failure -> Log.w("PlaceDetailViewModel", "Location wrong ${it.e}")
                    }
                }
                .addTo(disposables)
    }

    fun calculateRoute(origin: LatLng, destination: LatLng) {
        calculateRoute.calculateRoute(origin, destination)
                .firstOrError()
                .observeOn(schedulerProvider.ui())
                .toResult()
                .subscribe {
                    when (it) {
                        is Result.Success -> route.postValue(it.data)
                        is Result.Failure -> Log.w("PlaceDetailViewModel", "Calculate route error ${it.e}")
                    }
                }
                .addTo(disposables)
    }
}