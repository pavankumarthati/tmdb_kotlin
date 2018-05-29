package com.careem.tmdb.careemapp

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.schedulers.Schedulers
import net.danlew.android.joda.JodaTimeAndroid
import java.util.function.Consumer

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

    fun getConfigurationLiveData() : LiveData<CareemAppConfig> {
        if (configurationLiveData.getValue() == null) {
            synchronized (this) {
                if (configurationLiveData.getValue() == null) {
                    appComponent.provideTmdbRepository().getConfiguration()
                            .subscribeOn(Schedulers.io())
                            .subscribe({config -> configurationLiveData.postValue(config)}, {throwable -> throwable.also {println()}})
                }
            }
        }
        return configurationLiveData;
    }
}