package com.careem.tmdb.careemapp

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

interface ViewModelListener<T> {

    fun pagedItemsLiveData() : LiveData<PagedList<T>>;

    fun contentStatusLiveData() : LiveData<LoadingStatus>;

    fun refresh() : Unit;
}