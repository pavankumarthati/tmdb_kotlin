package com.careem.tmdb.careemapp.view_model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.careem.tmdb.careemapp.common.LoadingStatus
import com.careem.tmdb.careemapp.daggers.IOSchedulerTransformer
import com.careem.tmdb.careemapp.data.LatestMovieItem
import com.careem.tmdb.careemapp.data.TmdbRepository
import com.careem.tmdb.careemapp.interactors.DataSourceFactory
import com.careem.tmdb.careemapp.interactors.LatestMoviesDataSource
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

class LatestMoviesViewModel: ViewModel() {

    lateinit var mViewModelListener : ViewModelInteractionListener<LatestMovieItem>
    lateinit var tmdbRepository: TmdbRepository
    lateinit var scheduler: IOSchedulerTransformer
    lateinit var dataSourceFactory: DataSourceFactory<Long, LatestMovieItem>

    fun init(tmdbRepository: TmdbRepository, scheduler: IOSchedulerTransformer, input: Map<String, String>?) : Unit {
        this.tmdbRepository = tmdbRepository
        this.scheduler = scheduler
        dataSourceFactory = object: DataSourceFactory<Long, LatestMovieItem>(tmdbRepository, scheduler) {
            override fun getDataSource(tmdbRepository: TmdbRepository, scheduler: ObservableTransformer<Any, Any>, input: Map<String, String>?): DataSource<Long, LatestMovieItem> {
                return LatestMoviesDataSource(tmdbRepository, scheduler, input);
            }
        }

        dataSourceFactory.input = input

        val config: PagedList.Config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .build();

        val latestMovies: LiveData<PagedList<LatestMovieItem>> = LivePagedListBuilder<Long, LatestMovieItem>(dataSourceFactory, config)
                .setBackgroundThreadExecutor { command -> Schedulers.newThread().scheduleDirect(command) }.build();

        mViewModelListener = object: ViewModelInteractionListener<LatestMovieItem> {

            override fun refresh(input: Any?) {
                dataSourceFactory.input = input as Map<String, String>?
                dataSourceFactory.mutableLiveData.value?.invalidate();
            }

            override fun  pagedItemsLiveData() : LiveData<PagedList<LatestMovieItem>> {
                return latestMovies;
            }

            override fun contentStatusLiveData() : LiveData<LoadingStatus> {
                return Transformations.switchMap(dataSourceFactory.mutableLiveData) {
                    input -> (input as LatestMoviesDataSource).statusLiveData
                }
            }
        }
    }
}