package com.careem.tmdb.careemapp

import com.squareup.moshi.Json

data class CareemAppConfig(@Json(name = "images") var images: Images? = null,
                            @Json(name = "change_keys") var changeKeys: List<String>? = null) {

    data class Images(@Json(name = "base_url") var baseUrl: String? = null,
                      @Json(name = "secure_base_url") var secureBaseUrl: String? = null,
                      @Json(name = "backdrop_sizes") var backdropSizes: List<String>? = null,
                      @Json(name = "logo_sizes") var logoSizes: List<String>? = null,
                      @Json(name = "poster_sizes") var posterSizes: List<String>? = null,
                      @Json(name = "profile_sizes") var profileSizes: List<String>? = null,
                      @Json(name = "still_sizes") var stillSizes: List<String>? = null)
}