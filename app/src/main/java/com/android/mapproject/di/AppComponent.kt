package com.android.mapproject.di

import android.app.Activity
import android.app.Application
import com.android.mapproject.di.androidx.AndroidXInjectionModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.DispatchingAndroidInjector
import javax.inject.Singleton

/**
 * Created by JasonYang.
 */
@Singleton
@Component(modules = [
    AppModule::class,
    MainActivityBuilder::class,
    DataModule::class,
    ApiModule::class,
    MapServiceModule::class,
    ViewModelModule::class,
    AndroidXInjectionModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    val activityInjector: DispatchingAndroidInjector<Activity>
}