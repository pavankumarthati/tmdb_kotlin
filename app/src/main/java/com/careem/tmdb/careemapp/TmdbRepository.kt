package com.careem.tmdb.careemapp

import android.content.SharedPreferences
import io.reactivex.Observable

class TmdbRepository private constructor(var latestMovieApi: LatestMovieApi, var configurationApi: ConfigurationApi,
                                         var sharedPreferences: SharedPreferences): LatestMovieApi {


    companion object : HOLDER<TmdbRepository, LatestMovieApi, ConfigurationApi, SharedPreferences>(::TmdbRepository)

    fun getSessionInPrefs() : String = sharedPreferences.getString(SESSION_KEY, null)
    fun storeSessionInPrefs(session: String) : Unit {
        sharedPreferences.edit()
                .putString(SESSION_KEY, session)
                .apply()
    }

    override fun getLatestMovieDetail(page: Long, filters: Map<String, String>?): Observable<LatestMoviesResponse> = latestMovieApi.getLatestMovieDetail(page, filters)

    fun getConfiguration() : Observable<CareemAppConfig> = configurationApi.getConfiguration()
}


open class HOLDER<out T, in A, in B, in C>(creator: (A, B, C) -> T) {
    private var creator: ((A, B, C) -> T)? = creator
    @kotlin.jvm.Volatile private var instance: T? = null

    fun getInstance(arg1: A, arg2: B, arg3: C) : T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                return i2
            } else {
                val created = creator!!(arg1, arg2, arg3)
                instance = created
                creator = null
                return instance!!
            }
        }
    }
}