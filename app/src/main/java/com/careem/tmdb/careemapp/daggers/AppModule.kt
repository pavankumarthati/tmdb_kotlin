package com.careem.tmdb.careemapp.daggers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.careem.tmdb.careemapp.common.SHARED_PREFS_NAME
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideContext() : Context {
        return app;
    }

    @Provides
    @Singleton
    @IoScheduler
    fun provideIoScheduler() : Scheduler = Schedulers.io()

    @Provides
    @Singleton
    @MainScheduler
    fun provideMainScheduler() : Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    fun getSharedPrefs() : SharedPreferences = app.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)


}