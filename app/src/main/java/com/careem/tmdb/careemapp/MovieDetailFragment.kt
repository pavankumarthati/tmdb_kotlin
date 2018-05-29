package com.careem.tmdb.careemapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

const val MOVIE_ID_EXTRA = "movie_id_extra"

class MovieDetailFragment: Fragment() {
    private lateinit var movieDetailViewModel: MovieDetailViewModel;
    private lateinit var title: TextView;
    private lateinit var overview: TextView;
    private lateinit var backdropImg: ImageView;
    private var movieId: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getLong(MOVIE_ID_EXTRA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.movie_detail_layout, container, false)
        title = rootView.findViewById(R.id.title)
        overview = rootView.findViewById(R.id.overview)
        backdropImg = rootView.findViewById(R.id.backdropImg)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        val tmdbRepository = (activity?.application as? CareemApplication)?.appComponent?.provideTmdbRepository()

        movieDetailViewModel.init(movieId!!, tmdbRepository!!, IOSchedulerTransformer())
        movieDetailViewModel.getLiveData().observe(this, object: Observer<MovieDetailUseCase.Output> {
            override fun onChanged(output: MovieDetailUseCase.Output?) {
                output?.let { output ->
                    title.text = output.title
                    overview.text = output.overview
                    activity?.let {activity ->
                        (activity.application as CareemApplication).getConfigurationLiveData()
                                .observe(activity, Observer {config ->
                                    config?.let {
                                        Glide.with(activity.applicationContext)
                                                .load(config.images?.baseUrl + config.images?.posterSizes?.get(1)
                                                        + output.posterPath)
                                                .into(backdropImg)
                                    }


                                })
                    }
                }
            }

        })

    }

    companion object {
        @JvmStatic
        fun getInstance(movieId: Long) = MovieDetailFragment().apply {
            this.arguments = Bundle().apply {
                putLong(MOVIE_ID_EXTRA, movieId)
            }
        }
    }
}