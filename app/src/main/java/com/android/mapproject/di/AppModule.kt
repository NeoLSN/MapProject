package com.android.mapproject.di

import android.app.Application
import android.content.Context
import com.android.mapproject.util.rx.AppSchedulerProvider
import com.android.mapproject.util.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by JasonYang.
 */
@Module
internal object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    @JvmStatic
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}