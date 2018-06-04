package com.careem.tmdb.careemapp.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.careem.tmdb.careemapp.*
import com.careem.tmdb.careemapp.common.LoadingStatus
import com.careem.tmdb.careemapp.daggers.IOSchedulerTransformer
import com.careem.tmdb.careemapp.data.LatestMovieItem
import com.careem.tmdb.careemapp.data.TmdbRepository
import com.careem.tmdb.careemapp.view_model.LatestMoviesViewModel
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainContentFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainContentFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainContentFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var latestMoviesRv: RecyclerView
    private lateinit var filterFab: FloatingActionButton
    private lateinit var latestMoviesViewModel: LatestMoviesViewModel
    private var listener: OnFragmentInteractionListener? = null
    // filters
    private var input: HashMap<String, String>? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            input = HashMap()
            it.keySet().forEach {key ->
                input?.put(key, it.getString(key))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_content, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
    }

    private fun setupViews(view: View) {
        filterFab = view.findViewById(R.id.show_filter_fab)
        filterFab.hide()
        latestMoviesRv = rootView.findViewById(R.id.moviesRv)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        latestMoviesRv.layoutManager = llm
        val spaceItemDecoration = SpaceItemDecoration(60);
        latestMoviesRv.addItemDecoration(spaceItemDecoration);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initFab()
        initViewModel()
    }

    private fun initViewModel() {
        latestMoviesViewModel = ViewModelProviders.of(this).get(LatestMoviesViewModel::class.java)

        val tmdbRepository : TmdbRepository = (activity?.application as CareemApplication).appComponent.provideTmdbRepository()

        latestMoviesViewModel.init(tmdbRepository, IOSchedulerTransformer(), input);


        latestMoviesViewModel.mViewModelListener.pagedItemsLiveData().observe(this, object: Observer<PagedList<LatestMovieItem>> {
            override fun onChanged(list: PagedList<LatestMovieItem>?) {
                (latestMoviesRv.adapter as LatestMovieAdapter).submitList(list)
                filterFab.show()
            }

        });

        latestMoviesViewModel.mViewModelListener.contentStatusLiveData().observe(this, object: Observer<LoadingStatus> {
            override fun onChanged(t: LoadingStatus?) {
                (latestMoviesRv.adapter as LatestMovieAdapter).loadingStatus = t
            }

        });

        latestMoviesRv.addOnItemTouchListener(RecyclerViewItemClickListener(context!!, object : ClickHandler {
            override fun onClick(view: View, position: Int) {
                val latestMovieItem: LatestMovieItem = latestMoviesRv.getChildViewHolder(view).itemView.tag as LatestMovieItem
                listener?.onItemClicked(latestMovieItem.id)
            }
        }))
    }

    private fun initFab() {
        filterFab.setOnClickListener { listener?.onFilterClicked() }
    }

    private fun initRecyclerView() {
        val latestMovieAdapter = LatestMovieAdapter(LatestMovieItem);
        latestMoviesRv.adapter = latestMovieAdapter
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun applyFilters(map: HashMap<String, String>) {
        input = map
        latestMoviesViewModel.mViewModelListener.refresh(input)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFilterClicked()
        fun onItemClicked(id: Long)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param sort Parameter 1.
         * @param filter Parameter 2.
         * @return A new instance of fragment MainContentFragment.
         */
        @JvmStatic
        fun newInstance(filter: Map<String, String>) =
                MainContentFragment().apply {
                    arguments = Bundle().apply {
                        filter.forEach { key, value -> putSerializable(key, value) }
                    }
                }
    }
}
