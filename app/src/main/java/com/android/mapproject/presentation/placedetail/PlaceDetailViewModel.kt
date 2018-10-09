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
import com.android.mapproject.util.rx.SchedulerProvider
import com.android.mapproject.util.zip
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 * Created by JasonYang.
 */
class PlaceDetailViewModel @Inject constructor(
        private val getPlace: GetParkingPlaceUseCase,
        private val getLocation: GetLocationUseCase,
        private val calculateRoute: CalculateRouteUseCase,
        private val transformer : CoordinateTransformer,
        private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    val place = ObservableField<ParkingPlace>()
    val coordinate = MutableLiveData<LatLng>()
    private val currentLocation = MutableLiveData<LatLng>()
    val locations = currentLocation.zip(coordinate)
    val route = MutableLiveData<List<LatLng>>()

    fun getParkingPlace(id: String) {
        disposables += getPlace.getPlace(id)
                .observeOn(schedulerProvider.ui())
                .firstOrError()
                .subscribeBy(
                        onSuccess = {
                            place.set(it)
                            val twdX = it.tw97x?.toDouble() ?: 0.0
                            val twdY = it.tw97y?.toDouble() ?: 0.0
                            val latLng = transformer.twd97ToWgs84(twdX, twdY)
                            coordinate.postValue(latLng)
                        },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }

    fun getCurrentLocation() {
        disposables += getLocation.getCurrentLocation()
                .observeOn(schedulerProvider.ui())
                .firstOrError()
                .subscribeBy(
                        onSuccess = { latLng ->
                            Log.i("PlaceDetailViewModel", "$latLng")
                            currentLocation.postValue(latLng)
                        },
                        onError = { e -> Log.w("PlaceDetailViewModel", "Location wrong $e") }
                )
    }

    fun calculateRoute(origin: LatLng, destination: LatLng) {
        disposables += calculateRoute.calculateRoute(origin, destination)
                .observeOn(schedulerProvider.ui())
                .firstOrError()
                .subscribeBy(
                        onSuccess = { decodedPath -> route.postValue(decodedPath) },
                        onError = { e -> Log.w("PlaceDetailViewModel", "Calculate route error $e") }
                )
    }
}