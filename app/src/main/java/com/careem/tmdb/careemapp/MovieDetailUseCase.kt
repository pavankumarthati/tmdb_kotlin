package com.careem.tmdb.careemapp

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class MovieDetailUseCase(val tmdbRepository: TmdbRepository, val scheduler: ObservableTransformer<Any, Any>) : UseCase<MovieDetailUseCase.Input, MovieDetailUseCase.Output>() {


    @SuppressWarnings("unchecked")
    override fun executeUseCase(input: Input?) : Observable<Output>? {
        return input?.let {
            tmdbRepository.getMovieDetail(it.movieId)
                    .compose(mapResponseToOutput())
                    .composeX(scheduler) as Observable<Output>
        }
    }

    fun  mapResponseToOutput() : ObservableTransformer<MovieDetailResponse, Output> {
        return object: ObservableTransformer<MovieDetailResponse, Output> {
            override fun apply(upstream: Observable<MovieDetailResponse>?): Observable<Output>? {
                return upstream?.flatMap {
                    Output(it.id, it.title, it.posterPath, it.budget,
                            it.genres, it.overview, it.revenue, it.releaseDate)
                            .also { println() }
                            .let {
                                Observable.just(it)
                            }

                }
            }
        }
    }

    data class Input(val movieId: Long)


    data class Output(val id: Int, val title: String?, val posterPath: String?, val budget: Int?, val genres: List<MovieDetailResponse.Genre>?,
                      val overview: String?, val revenue: Int?, val releaseDate: String?)

}