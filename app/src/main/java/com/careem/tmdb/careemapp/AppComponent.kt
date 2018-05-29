package com.careem.tmdb.careemapp

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataSourceModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity) : Unit

    @Singleton
    fun provideTmdbRepository() : TmdbRepository

}