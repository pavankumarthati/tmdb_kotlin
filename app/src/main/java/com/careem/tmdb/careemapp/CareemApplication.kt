package com.careem.tmdb.careemapp

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.careem.tmdb.careemapp.common.FETCH_CONFIG_JOB_ID
import com.careem.tmdb.careemapp.daggers.*
import com.careem.tmdb.careemapp.usecases.ConfigFetcherService
import net.danlew.android.joda.JodaTimeAndroid

class CareemApplication: Application() {

    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        initComponent()
        if (!appComponent.provideTmdbRepository().isFirstStart()) {
            val jobScheduler = this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(JobInfo.Builder(FETCH_CONFIG_JOB_ID, ComponentName(this, ConfigFetcherService::class.java))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(3*24*60*60*1000) // every three days
                    .build())

            jobScheduler.schedule(JobInfo.Builder(FETCH_CONFIG_JOB_ID, ComponentName(this, ConfigFetcherService::class.java))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setMinimumLatency(1)
                    .setOverrideDeadline(1)
                    .build())

            appComponent.provideTmdbRepository().setAppFirstStart(true)
        }
    }

    private fun initComponent() : Unit {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .dataSourceModule(DataSourceModule())
                .networkModule(NetworkModule())
                .build();
    }
}