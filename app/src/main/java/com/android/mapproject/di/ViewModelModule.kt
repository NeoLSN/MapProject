package com.android.mapproject.di

import androidx.lifecycle.ViewModelProvider
import com.android.mapproject.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by JasonYang.
 */
@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}