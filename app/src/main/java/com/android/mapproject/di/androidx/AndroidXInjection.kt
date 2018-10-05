package com.android.mapproject.di.androidx

import android.util.Log
import android.util.Log.DEBUG
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.internal.Beta
import dagger.internal.Preconditions.checkNotNull

@Beta
object AndroidXInjection {
    private val TAG = "dagger.androidx"

    fun inject(fragment: Fragment) {
        checkNotNull<Fragment>(fragment, "fragment")
        val hasAndroidXFragmentInjector = findHasAndroidXFragmentInjector(fragment)
        if (Log.isLoggable(TAG, DEBUG)) {
            Log.d(
                    TAG,
                    String.format(
                            "An injector for %s was found in %s",
                            fragment.javaClass.canonicalName,
                            hasAndroidXFragmentInjector.javaClass.canonicalName))
        }

        val fragmentInjector = hasAndroidXFragmentInjector.supportFragmentInjector()
        checkNotNull<AndroidInjector<Fragment>>(
                fragmentInjector,
                "%s.supportFragmentInjector() returned null",
                hasAndroidXFragmentInjector.javaClass)

        fragmentInjector.inject(fragment)
    }

    private fun findHasAndroidXFragmentInjector(fragment: Fragment): HasAndroidXFragmentInjector {
        var parentFragment = fragment
        while (parentFragment.parentFragment != null) {
            parentFragment = parentFragment.parentFragment!!
            if (parentFragment is HasAndroidXFragmentInjector) {
                return parentFragment
            }
        }
        val activity = fragment.activity
        if (activity is HasAndroidXFragmentInjector) {
            return activity
        }
        activity?.application?.let {
            if (it is HasAndroidXFragmentInjector) {
                return it
            }
        }

        throw IllegalArgumentException(
                String.format("No injector was found for %s", fragment.javaClass.canonicalName))
    }
}