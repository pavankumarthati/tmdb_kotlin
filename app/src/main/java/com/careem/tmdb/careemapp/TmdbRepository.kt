package com.careem.tmdb.careemapp

import android.content.SharedPreferences
import io.reactivex.Observable

class TmdbRepository private constructor(var latestMoviesApi: LatestMovieApi, var movieDetailApi: MovieDetailApi, var configurationApi: ConfigurationApi,
                                         var sharedPreferences: SharedPreferences): LatestMovieApi, MovieDetailApi {


    companion object : HOLDER<TmdbRepository, LatestMovieApi, MovieDetailApi, ConfigurationApi, SharedPreferences>(::TmdbRepository)

    fun getSessionInPrefs() : String = sharedPreferences.getString(SESSION_KEY, null)
    fun storeSessionInPrefs(session: String) : Unit {
        sharedPreferences.edit()
                .putString(SESSION_KEY, session)
                .apply()
    }

    override fun getLatestMovies(page: Long, filters: Map<String, String>?): Observable<LatestMoviesResponse> = latestMoviesApi.getLatestMovies(page, filters)

    override fun getMovieDetail(movieId: Long): Observable<MovieDetailResponse> = movieDetailApi.getMovieDetail(movieId)

    fun getConfiguration() : Observable<CareemAppConfig> = configurationApi.getConfiguration()
}


open class HOLDER<out T, in A, in B, in C, in D>(creator: (A, B, C, D) -> T) {
    private var creator: ((A, B, C, D) -> T)? = creator
    @kotlin.jvm.Volatile private var instance: T? = null

    fun getInstance(arg1: A, arg2: B, arg3: C, arg4: D) : T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                return i2
            } else {
                val created = creator!!(arg1, arg2, arg3, arg4)
                instance = created
                creator = null
                return instance!!
            }
        }
    }
}