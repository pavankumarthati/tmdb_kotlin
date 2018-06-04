package com.careem.tmdb.careemapp.data

import android.content.Context
import android.content.SharedPreferences
import com.careem.tmdb.careemapp.network.ConfigurationApi
import com.careem.tmdb.careemapp.network.LatestMovieApi
import com.careem.tmdb.careemapp.network.MovieDetailApi
import io.reactivex.Observable
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val CONFIG_FILE = "config_file.dat"
const val IS_FIRST_START_KEY = "is_first_start_key"
class TmdbRepository private constructor(var latestMoviesApi: LatestMovieApi, var movieDetailApi: MovieDetailApi, var configurationApi: ConfigurationApi,
                                         var sharedPreferences: SharedPreferences): LatestMovieApi, MovieDetailApi {

    companion object : HOLDER<TmdbRepository, LatestMovieApi, MovieDetailApi, ConfigurationApi, SharedPreferences>(::TmdbRepository)

    fun storeCareemAppConfig(config: CareemAppConfig, context: Context) {
        val fileOutputStream = context.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE)
        val oos = ObjectOutputStream(fileOutputStream)
        oos.writeObject(config)
    }

    fun fetchCareemAppConfig(context: Context) : CareemAppConfig? {
        val fileInputStream = context.openFileInput(CONFIG_FILE)
        val ois = ObjectInputStream(fileInputStream)
        return (ois.readObject() as? CareemAppConfig)
    }

    fun isFirstStart(): Boolean = sharedPreferences.getBoolean(IS_FIRST_START_KEY, false)

    fun setAppFirstStart(flag: Boolean) = sharedPreferences.edit()
                .putBoolean(IS_FIRST_START_KEY, flag)
                .apply()

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