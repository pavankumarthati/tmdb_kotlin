package com.careem.tmdb.careemapp

import io.reactivex.Observable
import retrofit2.http.GET

interface ConfigurationApi {
    @GET("3/configuration")
    fun getConfiguration() : Observable<CareemAppConfig>;
}