package com.careem.tmdb.careemapp

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

public class LatestMoviesUseCase(var tmdbRepository: TmdbRepository, var scheduler: ObservableTransformer<Any, Any>) : UseCase<LatestMoviesUseCase.Input, LatestMoviesUseCase.Output>() {

    @SuppressWarnings("unchecked")
    override fun executeUseCase(input: Input?): Observable<Output>? {
        return input?.let {
            tmdbRepository.getLatestMovies(it.page, it.filters)
                    .compose(mapResponseToOutput())
                    .composeX(scheduler) as Observable<Output>


        }
    }

    private fun mapResponseToOutput() : ObservableTransformer<LatestMoviesResponse, Output> {
        return object: ObservableTransformer<LatestMoviesResponse, Output> {
            override fun apply(upstream: Observable<LatestMoviesResponse>?): ObservableSource<Output>? {
                System.out.println("observable received $upstream")
                return upstream?.let {
                    upstream.flatMap {response ->
                        System.out.println("response received $response")
                        val list: Array<LatestMovieItem> = Array(response.results.size) {index ->
                            LatestMovieItem().apply {
                                this.id = response.results[index].id
                                this.title = response.results[index].title
                                this.backdropPath = response.results[index].backdrop_path
                                this.overview = response.results[index].overview
                                this.popularity = Math.round(response.results[index].popularity).toInt()
                                this.votes = response.results[index].vote_count
                                this.page = response.page
                                this.videoAvg = response.results[index].video_average
                                this.posterPath = response.results[index].poster_path
                            }
                        }
                        Observable.just(Output(list))
                    }
                }
            }
        }
    }

    data class Input(var page: Long, var filters: Map<String, String>?)

    data class Output(var items: Array<LatestMovieItem>)
}



