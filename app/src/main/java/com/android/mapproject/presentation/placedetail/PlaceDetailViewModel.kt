package com.android.mapproject.presentation.placedetail

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.domain.GetParkingPlaceUseCase
import com.android.mapproject.domain.ParkingPlace
import com.android.mapproject.presentation.BaseViewModel
import com.android.mapproject.util.coordinate.CoordinateTransformer
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by JasonYang.
 */
class PlaceDetailViewModel(
        private val getPlace: GetParkingPlaceUseCase
) : BaseViewModel() {

    private var transformer = CoordinateTransformer()

    val place = ObservableField<ParkingPlace>()
    val coordinate = MutableLiveData<LatLng>()

    fun getParkingPlace(id: String) {
        disposables += getPlace.getPlace(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            place.set(it)
                            val twdX = it.tw97x?.toDouble() ?: 0.0
                            val twdY = it.tw97y?.toDouble() ?: 0.0
                            val latLng = transformer.twd97ToWgs84(twdX, twdY)
                            coordinate.value = latLng
                        },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }
}