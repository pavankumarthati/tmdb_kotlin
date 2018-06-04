package com.careem.tmdb.careemapp

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.careem.tmdb.careemapp.common.FETCH_CONFIG_JOB_ID
import com.careem.tmdb.careemapp.usecases.ConfigFetcherService

// currently not used in app
class BootCompletionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(BootCompletionReceiver::class.java.simpleName, "receiver invoked")
        val action = intent?.action
        if (action == Intent.ACTION_INSTALL_PACKAGE) {
            val jobScheduler = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(JobInfo.Builder(FETCH_CONFIG_JOB_ID, ComponentName(context, ConfigFetcherService::class.java))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .build()
            )
        }
    }
}