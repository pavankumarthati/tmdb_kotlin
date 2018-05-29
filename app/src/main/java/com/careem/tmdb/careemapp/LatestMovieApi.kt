package com.careem.tmdb.careemapp

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface LatestMovieApi {
    @GET("3/discover/movie")
    fun getLatestMovies(@Query("page") page: Long,
                        @QueryMap filters: Map<String, String>?): Observable<LatestMoviesResponse>

}
