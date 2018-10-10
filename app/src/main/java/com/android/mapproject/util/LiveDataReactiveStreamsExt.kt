package com.android.mapproject.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * Created by JasonYang.
 */
fun <T> Publisher<T>.toLiveData(): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this)

fun <T> Flowable<T>.toLiveData(): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this)

fun <T> Observable<T>.toLiveData(backPressureStrategy: BackpressureStrategy): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this.toFlowable(backPressureStrategy))

fun <T> Single<T>.toLiveData(): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this.toFlowable())

fun <T> Maybe<T>.toLiveData(): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this.toFlowable())


fun <T> Completable.toLiveData(): LiveData<T> =
        LiveDataReactiveStreams.fromPublisher(this.toFlowable())
