package com.android.mapproject.presentation.common

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by JasonYang.
 */
@CheckResult
fun <T> Flowable<T>.toResult(): Flowable<Result<T>> =
        compose { item ->
            item
                    .map { Result.success(it) }
                    .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                    .startWith(Result.inProgress())
        }

@CheckResult
fun <T> Observable<T>.toResult(): Observable<Result<T>> =
        compose { item ->
            item
                    .map { Result.success(it) }
                    .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                    .startWith(Result.inProgress())
        }

@CheckResult
fun <T> Single<T>.toResult(): Observable<Result<T>> = toObservable().toResult()

@CheckResult
fun Completable.toResult(): Observable<Result<Unit>> = toSingleDefault(Unit).toResult()
