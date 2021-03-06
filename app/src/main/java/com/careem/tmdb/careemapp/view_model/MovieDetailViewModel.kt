package com.careem.tmdb.careemapp.view_model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.careem.tmdb.careemapp.data.TmdbRepository
import com.careem.tmdb.careemapp.usecases.MovieDetailUseCase
import io.reactivex.ObservableTransformer

class MovieDetailViewModel: ViewModel() {
    private val mutableLiveData: MutableLiveData<MovieDetailUseCase.Output> = MutableLiveData()

    fun init(movieId: Long, tmdbRepository: TmdbRepository, scheduler: ObservableTransformer<Any, Any>) {
        val movieDetailUseCase = MovieDetailUseCase(tmdbRepository, scheduler);
        movieDetailUseCase.mInput = MovieDetailUseCase.Input(movieId)
        movieDetailUseCase.run()?.subscribe( { output -> mutableLiveData.postValue(output) },
                        {throwable -> System.out.println("error " + throwable)})
    }

    fun getLiveData() : LiveData<MovieDetailUseCase.Output> {
        return mutableLiveData;
    }

}