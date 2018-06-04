package com.careem.tmdb.careemapp.common

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

fun  <T> Observable<T>.composeX(arg: ObservableTransformer<in Any, out Any>): Observable<out Any> {
    return Observable.wrap(arg.apply(this))
}