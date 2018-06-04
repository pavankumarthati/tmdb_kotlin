package com.careem.tmdb.careemapp.network

import com.careem.tmdb.careemapp.data.CareemAppConfig
import io.reactivex.Observable
import retrofit2.http.GET

interface ConfigurationApi {
    @GET("3/configuration")
    fun getConfiguration() : Observable<CareemAppConfig>;
}