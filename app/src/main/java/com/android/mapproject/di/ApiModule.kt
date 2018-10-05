package com.android.mapproject.di

import com.android.mapproject.data.source.remote.ParkingPlaceService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by JasonYang.
 */
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideHttpLogger(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    @Singleton
    @Provides
    fun provideHttpClient(logger: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
            .setLenient()
            .create()

    @Singleton
    @Provides
    fun provideApiService(httpClient: OkHttpClient, gson: Gson) = Retrofit.Builder()
            .baseUrl(ParkingPlaceService.HTTPS_API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
            .create(ParkingPlaceService::class.java)

}