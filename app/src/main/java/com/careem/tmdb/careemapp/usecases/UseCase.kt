package com.careem.tmdb.careemapp.usecases

import io.reactivex.Observable

abstract class UseCase<P, Q> {

    var mInput: P? = null

    fun run() : Observable<Q>? {
        return executeUseCase(mInput);
    }

    abstract fun executeUseCase(input: P?) : Observable<Q>?;
}