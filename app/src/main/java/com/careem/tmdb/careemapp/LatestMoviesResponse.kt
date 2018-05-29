package com.careem.tmdb.careemapp

data class LatestMoviesResponse(var page: Int, var total_pages: Int, var results: Array<LatestMoviesResponseItem>) {

    data class LatestMoviesResponseItem(var vote_count: Int, var id: Long, var video_average: Double, var video: Boolean,
                                        var title: String, var popularity: Double, var poster_path: String, var original_language: String,
                                        var original_title: String, var genre_ids: Array<Int>, var backdrop_path: String,
                                        var adult: Boolean, var overview: String, var release_date: String)

}