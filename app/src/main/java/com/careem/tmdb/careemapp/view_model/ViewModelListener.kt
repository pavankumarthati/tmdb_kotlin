package com.careem.tmdb.careemapp.view_model

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.careem.tmdb.careemapp.common.LoadingStatus

interface ViewModelListener<T> {

    fun pagedItemsLiveData() : LiveData<PagedList<T>>;

    fun contentStatusLiveData() : LiveData<LoadingStatus>;

    fun refresh() : Unit;
}