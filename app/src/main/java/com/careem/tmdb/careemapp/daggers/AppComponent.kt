package com.careem.tmdb.careemapp.daggers

import com.careem.tmdb.careemapp.data.TmdbRepository
import com.careem.tmdb.careemapp.views.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataSourceModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity) : Unit

    @Singleton
    fun provideTmdbRepository() : TmdbRepository

}