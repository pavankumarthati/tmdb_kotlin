package com.careem.tmdb.careemapp

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun providesLatestMoviesApi(retrofit: Retrofit) = retrofit.create(LatestMovieApi::class.java)

    @Provides
    @Singleton
    fun providesMovieDetailApi(retrofit: Retrofit) = retrofit.create(MovieDetailApi::class.java)

    @Provides
    @Singleton
    fun providesConfigurationApi(retrofit: Retrofit) = retrofit.create(ConfigurationApi::class.java)

    @Provides
    @Singleton
    fun providesTmdbRepository(latestMovieApi: LatestMovieApi, movieDetailApi: MovieDetailApi, configurationApi: ConfigurationApi,
    sharedPreferences: SharedPreferences) = TmdbRepository.getInstance(latestMovieApi, movieDetailApi, configurationApi, sharedPreferences)

}