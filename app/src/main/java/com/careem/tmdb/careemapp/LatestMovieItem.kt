package com.careem.tmdb.careemapp

import android.support.v7.recyclerview.extensions.DiffCallback

data class LatestMovieItem(var id: Long = 0, var page: Int = 0, var popularity: Int = 0, var votes : Int = 0, var title: String? = null, var posterPath: String? = null,
                           var backdropPath: String? = null, var overview: String? = null, var videoAvg: Double? = null) {

    companion object DIFF_CALLBACK : DiffCallback<LatestMovieItem>() {

        override fun areContentsTheSame(oldItem: LatestMovieItem, newItem: LatestMovieItem): Boolean = oldItem.id == newItem.id

        override fun areItemsTheSame(oldItem : LatestMovieItem, newItem : LatestMovieItem) : Boolean = oldItem.equals(newItem)


    }
}