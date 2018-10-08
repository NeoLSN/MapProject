package com.android.mapproject.di

import com.android.mapproject.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by JasonYang.
 */
@Module
interface MainActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun contributeMainActivity(): MainActivity
}