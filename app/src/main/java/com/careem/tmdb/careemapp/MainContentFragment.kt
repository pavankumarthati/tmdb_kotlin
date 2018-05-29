package com.careem.tmdb.careemapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collector
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
    private lateinit var showFilterFab: FloatingActionButton
    private lateinit var latestMoviesViewModel: LatestMoviesViewModel
    private var refreshData: Boolean = true
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
        showFilterFab = view.findViewById(R.id.show_filter_fab)
        latestMoviesRv = rootView.findViewById(R.id.moviesRv)
        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        latestMoviesRv.layoutManager = llm
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        latestMoviesViewModel = ViewModelProviders.of(this).get(LatestMoviesViewModel::class.java)

        val tmdbRepository : TmdbRepository = (activity?.application as CareemApplication).appComponent.provideTmdbRepository()

        latestMoviesViewModel.init(tmdbRepository, IOSchedulerTransformer(), input);

        val latestMovieAdapter = LatestMovieAdapter(LatestMovieItem.DIFF_CALLBACK);

        latestMoviesViewModel.mViewModelListener.pagedItemsLiveData().observe(this, object: Observer<PagedList<LatestMovieItem>> {
            override fun onChanged(list: PagedList<LatestMovieItem>?) {
                latestMovieAdapter.submitList(list)
                System.out.println("Data Loaded....");
            }

        });

        latestMoviesViewModel.mViewModelListener.contentStatusLiveData().observe(this, object: Observer<LoadingStatus> {
            override fun onChanged(t: LoadingStatus?) {
                latestMovieAdapter.loadingStatus = t
                System.out.println("Loading....");
            }

        });

        val spaceItemDecoration = SpaceItemDecoration(60);
        latestMoviesRv.addItemDecoration(spaceItemDecoration);
        activity?.let {
            latestMoviesRv.addOnItemTouchListener(RecyclerViewItemClickListener(it, object: ClickHandler {
                override fun onClick(view: View, position: Int) {
                    val latestMovieItem: LatestMovieItem = latestMoviesRv.getChildViewHolder(view).itemView.tag as LatestMovieItem
                    listener?.onItemClicked(latestMovieItem.id)
                }
            }))
        }

        latestMoviesRv.adapter = latestMovieAdapter
        showFilterFab.setOnClickListener { listener?.onFilterClicked() }
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
