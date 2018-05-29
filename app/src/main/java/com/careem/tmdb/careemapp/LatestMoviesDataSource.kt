package com.careem.tmdb.careemapp

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class LatestMoviesDataSource(var tmdbRepository: TmdbRepository,
                             var scheduler: ObservableTransformer<Any, Any>,
                             var input: Map<String, String>?) : PageKeyedDataSource<Long, LatestMovieItem>() {

    var  statusLiveData : MutableLiveData<LoadingStatus> = MutableLiveData();


    override fun loadInitial(params : LoadInitialParams<Long> ,
            callback : LoadInitialCallback<Long, LatestMovieItem> ) : Unit {
        val input = LatestMoviesUseCase.Input(1, this.input);
        val latestMoviesUseCase = LatestMoviesUseCase(tmdbRepository, scheduler);
        latestMoviesUseCase.mInput = input
        statusLiveData.postValue(LoadingStatus(Status.LOADING));
        latestMoviesUseCase.run()?.
                subscribe(object: Observer<LatestMoviesUseCase.Output> {

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(value: LatestMoviesUseCase.Output) {
                        statusLiveData.postValue(LoadingStatus(Status.LOADED));
                        callback.onResult(value.items.toList(), input.page - 1, input.page + 1);
                    }


                    override fun onError(e: Throwable) : Unit {
                        System.out.println("error occurred " + e)
                        statusLiveData.postValue(LoadingStatus("Unable to load data", Status.FAILED))
                    }


                    override fun onComplete() : Unit {
                    }
                });
    }


    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, LatestMovieItem>) : Unit {
        System.out.println("loading before " + params.key);
    }


    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, LatestMovieItem> ) : Unit {
        System.out.println("Loading Range " + params.key + " size: " + params.requestedLoadSize);
        statusLiveData.postValue(LoadingStatus(Status.LOADING));
        val latestMoviesUseCase = LatestMoviesUseCase(tmdbRepository, scheduler);
        val input = LatestMoviesUseCase.Input(params.key, this.input);
        latestMoviesUseCase.mInput = input;
        latestMoviesUseCase.run()?.subscribe(object:  Observer<LatestMoviesUseCase.Output> {

                    override fun onSubscribe(d: Disposable) : Unit {

                    }


                    override fun onNext(value: LatestMoviesUseCase.Output): Unit {
                        statusLiveData.postValue( LoadingStatus(Status.LOADED));
                        callback.onResult(value.items.toList(), input.page + 1);
                    }


                    override fun onError(e: Throwable) : Unit {
                        statusLiveData.postValue(LoadingStatus("Unable to load data", Status.FAILED));
                    }

                    override fun onComplete() : Unit {

                    }
                });
    }
}