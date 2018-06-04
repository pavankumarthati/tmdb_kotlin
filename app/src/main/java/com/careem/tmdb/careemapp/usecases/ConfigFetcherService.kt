package com.careem.tmdb.careemapp.usecases

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.careem.tmdb.careemapp.CareemApplication
import io.reactivex.schedulers.Schedulers

class ConfigFetcherService: JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(ConfigFetcherService::class.java.simpleName, "onstartJob called")
        (application as CareemApplication).appComponent.provideTmdbRepository().getConfiguration()
                .subscribeOn(Schedulers.io())
                .doOnNext {careemConfig ->
                    careemConfig?.let {
                        (application as CareemApplication).appComponent.provideTmdbRepository().storeCareemAppConfig(it, applicationContext)
                    }
                    jobFinished(params, false)
                }
                .doOnError {
                    Log.e(ConfigFetcherService::class.java.simpleName, "unable to save config to file")
                    jobFinished(params, false)
                }
                .subscribe()
        return true;
    }


    override fun onStopJob(params: JobParameters?): Boolean {
        return false;
    }
}