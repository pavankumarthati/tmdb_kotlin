package com.careem.tmdb.careemapp.interactors

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.careem.tmdb.careemapp.data.TmdbRepository
import io.reactivex.ObservableTransformer

abstract class DataSourceFactory<T, Q>(var tmdbRepository: TmdbRepository, var scheduler: ObservableTransformer<Any, Any>,
                                       var mutableLiveData: MutableLiveData<DataSource<T, Q>> = MutableLiveData())
    : DataSource.Factory<T, Q> {

    var input: Map<String, String>? = null

    abstract fun getDataSource(tmdbRepository : TmdbRepository, scheduler : ObservableTransformer<Any, Any>, input: Map<String, String>?) : DataSource<T, Q>

    override fun create() : DataSource<T, Q> {
        val dataSource = getDataSource(tmdbRepository, scheduler, input);
        mutableLiveData.postValue(dataSource);
        return dataSource;
    }
}