package com.android.mapproject.presentation.common

/**
 * Created by JasonYang.
 */
sealed class Result<T>(val inProgress: Boolean) {

    class InProgress<T> constructor(
            val isIndeterminate: Boolean,
            val steps: Int,
            val progress: Int
    ) : Result<T>(true) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int = javaClass.hashCode()
    }

    data class Success<T>(var data: T) : Result<T>(false)

    data class Failure<T>(val errorMessage: String?, val e: Throwable) : Result<T>(false)

    companion object {
        fun <T> inProgress(isIndeterminate: Boolean = true, steps: Int = 0, progress: Int = 0): Result<T>
                = InProgress(isIndeterminate, steps, progress)

        fun <T> success(data: T): Result<T> = Success(data)

        fun <T> failure(errorMessage: String?, e: Throwable): Result<T> = Failure(errorMessage, e)
    }
}