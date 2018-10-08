package com.android.mapproject.presentation.places

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.mapproject.domain.model.ParkingPlace
import com.android.mapproject.domain.usecase.FilterParkingPlacesUseCase
import com.android.mapproject.domain.usecase.GetParkingPlacesUseCase
import com.android.mapproject.domain.usecase.RefreshParkingPlacesUseCase
import com.android.mapproject.presentation.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created by JasonYang.
 */
class ParkingPlacesViewModel(
        private val refresh: RefreshParkingPlacesUseCase,
        private val getPlaces: GetParkingPlacesUseCase,
        private val filter: FilterParkingPlacesUseCase
) : BaseViewModel() {

    val places = MutableLiveData<List<ParkingPlace>>()
    val isRefreshing by lazy {
        val bool = MutableLiveData<Boolean>()
        bool.postValue(false)
        bool
    }
    val searchTerm = ObservableField<String>()

    private var isLoaded = false
    private val subject = PublishSubject.create<String>()

    init {
        val worker = subject
                .debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext { searchTerm.set(it) }
                .doOnNext { isRefreshing.postValue(true) }
                .switchMap { str ->
                    if (str.isBlank()) getPlaces.allPlaces().toObservable()
                    else filter.filter(str).toObservable()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isRefreshing.postValue(false) }
                .subscribeBy(
                        onNext = { places.postValue(it) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Filter places error: $e") }
                )
        disposables += worker
    }

    fun allPlaces(forceReload: Boolean = false) {
        if (isLoaded && !forceReload) return
        disposables += getPlaces.allPlaces()
                .doOnSubscribe { isRefreshing.postValue(true) }
                .subscribeOn(Schedulers.io())
                .firstOrError()
                .doAfterSuccess { isRefreshing.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            isLoaded = true
                            if (it.isEmpty()) refreshParkingPlaces()
                            places.postValue(it)
                        },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Get places error: $e") }
                )
    }

    fun refreshParkingPlaces() {
        if (isRefreshing.value == true) return
        disposables += refresh.refreshParkingPlaces()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { isRefreshing.postValue(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { isRefreshing.postValue(false) }
                .subscribeBy(
                        onComplete = { allPlaces(true) },
                        onError = { e -> Log.w("ParkingPlacesViewModel", "Refresh error: $e") }
                )
    }

    fun filterPlaces(term: String) {
        subject.onNext(term)
    }

    override fun onCleared() {
        super.onCleared()
        subject.onComplete()
    }
}