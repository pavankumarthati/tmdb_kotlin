package com.careem.tmdb.careemapp

import com.squareup.moshi.Json

data class MovieDetailResponse(@Json(name = "adult") var adult: Boolean?,
                               @Json(name = "backdrop_path") var backdropPath: String?,
                               @Json(name = "belongs_to_collection") var belongsToCollection: BelongsToCollection?,
                               var budget: Int?,
                               var genres: List<Genre>?,
                               var homepage: String?,
                               var id: Int,
                               @Json(name = "imdb_id") var imdbId: String?,
                               @Json(name = "original_language") var originalLanguage: String?,
                               @Json(name = "original_title") var originalTitle: String?,
                               var overview: String?,
                               var popularity: Double?,
                               @Json(name = "poster_path") var posterPath: String?,
                               @Json(name = "production_companies") var productionCompanies: Array<ProductionCompany>?,
                               @Json(name = "production_countries") var productionCountries: Array<ProductionCountry>?,
                               @Json(name = "release_date") var releaseDate: String?,
                               var revenue: Int?,
                               var runtime: Int?,
                               @Json(name = "spoken_languages") var spokenLanguages: Array<SpokenLanguage>?,
                               var status: String?,
                               var tagline: String?,
                               var title: String?,
                               var video: Boolean?,
                               @Json(name = "vote_average") var voteAverage: Double?,
                               @Json(name = "vote_count") var voteCount: Int?
                               ) {

    data class ProductionCompany(var id: Int, @Json(name = "logo_path") var logoPath: String?, var name: String?,
                                 @Json(name = "origin_country") var originCountry: String?)

    data class SpokenLanguage(@Json(name = "iso_639_1") var iso6391: String?, var name: String?)

    data class BelongsToCollection(var id: Int?, var name: String?, var postPath: String?, var backdropPath: String?)

    data class Genre(var id: Int?, var name: String?)

    data class ProductionCountry(@Json(name = "iso_3166_1") var iso31661: String?, var name: String?)

}