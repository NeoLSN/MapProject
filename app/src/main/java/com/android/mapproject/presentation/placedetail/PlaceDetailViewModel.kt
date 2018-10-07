package com.android.mapproject.presentation.placedetail

import android.util.Log
import androidx.databinding.ObservableField
import com.android.mapproject.domain.GetParkingPlaceUseCase
import com.android.mapproject.domain.ParkingPlace
import com.android.mapproject.presentation.BaseViewModel
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by JasonYang.
 */
class PlaceDetailViewModel(
        private val getPlace: GetParkingPlaceUseCase
) : BaseViewModel() {

    val place = ObservableField<ParkingPlace>()

    fun getParkingPlace(id: String) {
        disposables += getPlace.getPlace(id)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = { place.set(it) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }
}