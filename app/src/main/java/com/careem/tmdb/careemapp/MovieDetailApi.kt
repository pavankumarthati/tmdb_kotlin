package com.careem.tmdb.careemapp

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDetailApi {

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId: Long): Observable<MovieDetailResponse>

}