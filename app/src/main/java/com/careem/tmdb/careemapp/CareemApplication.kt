package com.careem.tmdb.careemapp

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import net.danlew.android.joda.JodaTimeAndroid

class CareemApplication: Application() {

    lateinit var appComponent : AppComponent
    lateinit var configurationLiveData : MutableLiveData<CareemAppConfig>

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        initComponent()
    }

    private fun initComponent() : Unit {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .dataSourceModule(DataSourceModule())
                .networkModule(NetworkModule())
                .build();
        configurationLiveData = MutableLiveData();
    }
}