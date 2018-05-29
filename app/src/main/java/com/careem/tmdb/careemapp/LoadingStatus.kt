package com.careem.tmdb.careemapp

data class LoadingStatus(var status: Status) {
    var reason: String? = null
    constructor(reason: String, status : Status) : this(status) {
        this.reason = reason
    }
}


enum class Status {
    LOADING, LOADED, FAILED
}