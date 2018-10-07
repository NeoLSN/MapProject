package com.android.mapproject.presentation.places

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.domain.GetParkingPlacesUseCase
import com.android.mapproject.domain.ParkingPlace
import com.android.mapproject.domain.RefreshParkingPlacesUseCase
import com.android.mapproject.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by JasonYang.
 */
class ParkingPlacesViewModel(
        private val refresh: RefreshParkingPlacesUseCase,
        private val getPlaces: GetParkingPlacesUseCase
) : BaseViewModel() {

    val places = MutableLiveData<List<ParkingPlace>>()
    val isRefreshing = ObservableBoolean(false)

    private var isLoaded = false

    fun allPlaces(forceReload: Boolean = false) {
        if (isLoaded && !forceReload) return
        disposables += getPlaces.allPlaces()
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onSuccess = {
                            isLoaded = true
                            if (it.isEmpty()) refreshParkingPlaces()
                            places.value = it
                        },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }

    fun refreshParkingPlaces() {
        if (isRefreshing.get()) return
        disposables += refresh.refreshParkingPlaces()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { isRefreshing.set(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { isRefreshing.set(false) }
                .subscribeBy(
                        onComplete = { allPlaces(true) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Refresh error: $e") }
                )
    }
}