package com.android.mapproject

import android.app.Application
import com.android.mapproject.di.AppComponent
import com.android.mapproject.di.DaggerAppComponent
import dagger.android.HasActivityInjector

/**
 * Created by JasonYang.
 */
class MapProjectApp : Application(), HasActivityInjector {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun activityInjector() = appComponent.activityInjector
}
