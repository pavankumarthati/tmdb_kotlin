package com.careem.tmdb.careemapp

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

class IOSchedulerTransformer : ObservableTransformer<Any, Any> {
    override fun apply(upstream: Observable<Any>?): ObservableSource<Any> = upstream!!.subscribeOn(Schedulers.io())
}