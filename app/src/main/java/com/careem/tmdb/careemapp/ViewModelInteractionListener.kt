package com.careem.tmdb.careemapp

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

interface ViewModelInteractionListener<T> {

    fun pagedItemsLiveData() : LiveData<PagedList<T>>

    fun contentStatusLiveData() : LiveData<LoadingStatus>

    fun refresh(input: Any?) : Unit
}