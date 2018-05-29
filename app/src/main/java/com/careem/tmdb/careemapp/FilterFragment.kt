package com.careem.tmdb.careemapp

import android.arch.persistence.room.util.StringUtil
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatRadioButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.movie_filters.*
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FilterFragment.OnFiltersChangedListener] interface
 * to handle interaction events.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

const val SORT_BY: String = "sort_by"
const val RELEASE_DATE_ASC = "release_date.asc"
const val RELEASE_DATE_DESC = "release_date.desc"
const val RELEASE_DATE_GTE = "release_date_gte"
const val RELEASE_DATE_LTE = "release_date_lte"


class FilterFragment : Fragment() {

    private var initialFilterMap: HashMap<String, String>? = null
    private var changedFilterMap: HashMap<String, String>? = null
    private var resetFilterMap: HashMap<String, String>? = null
    private var listener: OnFiltersChangedListener? = null

    private lateinit var sortByTv: TextView
    private lateinit var filterByTv: TextView
    private lateinit var confirmBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var filterContentFrame: FrameLayout
    private var sortByView: RadioGroup? = null
    private var moviefilterView: ConstraintLayout? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFiltersChangedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        populateData(arguments!!)
    }

    private fun populateData(input: Bundle): Unit {
        resetFilterMap = HashMap<String, String>().apply {
            input.keySet().forEach {
                this[it] = input.get(it) as String
            }
        }
        initialFilterMap = resetFilterMap
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_filter, container, false)
        sortByTv = rootView.findViewById(R.id.sort_by_label)
        filterByTv = rootView.findViewById(R.id.filter_by_label)
        confirmBtn = rootView.findViewById(R.id.confirm_btn)
        resetBtn = rootView.findViewById(R.id.reset_btn)
        filterContentFrame = rootView.findViewById(R.id.filter_content)
        initializeSortingView(context)
        initializeFilterView(context)
        filterContentFrame.addView(sortByView)
        sortByTv.isSelected = true
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sortByTv.setOnClickListener { v ->
            when(v?.isSelected) {
                false -> {
                    filterContentFrame.removeAllViews()
                    filterByTv.isSelected = false
                    sortByTv.isSelected = true
                    filterContentFrame.addView(sortByView)
                }
            }
        }

        filterByTv.setOnClickListener { v ->
            when(v?.isSelected) {
                false -> {
                    filterContentFrame.removeAllViews()
                    sortByTv.isSelected = false
                    filterByTv.isSelected = true
                    filterContentFrame.addView(moviefilterView)
                }
            }
        }

        confirmBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                updateFilterMap()
            }

        })
    }

    private fun updateFilterMap() {
        sortByView?.let {
            when(it.checkedRadioButtonId) {
                R.id.releaseDateDesc -> {
                    initialFilterMap?.put(SORT_BY, RELEASE_DATE_DESC)
                }
                else -> {
                    initialFilterMap?.put(SORT_BY, RELEASE_DATE_ASC)
                }
            }
        }

        moviefilterView?.let {
            val releaseDateLte: CharSequence = (it.findViewById(R.id.releaseDateLte) as TextView).text
            val releaseDateGte: CharSequence = (it.findViewById(R.id.releaseDateGte) as TextView).text
            when {
                releaseDateLte.isNotEmpty() -> {
                    initialFilterMap?.put(RELEASE_DATE_LTE, releaseDateLte.toString())
                }
                releaseDateGte.isNotEmpty() -> {
                    initialFilterMap?.put(RELEASE_DATE_GTE, releaseDateGte.toString())
                }
                else -> {}
            }
        }
        listener?.onFiltersChanged(initialFilterMap!!)
    }

    private fun initializeSortingView(context: Context?) {
        context?.let {
            val layoutInflater = LayoutInflater.from(context)
            sortByView = layoutInflater.inflate(R.layout.sort_filters, filterContentFrame, false) as RadioGroup
            when(initialFilterMap?.get(SORT_BY)) {
                RELEASE_DATE_DESC -> {
                    (sortByView!!.getChildAt(0) as AppCompatRadioButton).isChecked = true
                    changedFilterMap?.set(SORT_BY, RELEASE_DATE_DESC)
                }
                RELEASE_DATE_ASC -> {
                    ((sortByView!!).getChildAt(1) as AppCompatRadioButton).isChecked = true
                    changedFilterMap?.set(SORT_BY, RELEASE_DATE_ASC)
                }
                else -> {
                    (sortByView!!.getChildAt(0) as AppCompatRadioButton).isChecked = true
                    changedFilterMap?.set(RELEASE_DATE_DESC, formatToSimpleDate(Date()))
                    initialFilterMap?.set(RELEASE_DATE_DESC, formatToSimpleDate(Date()))
                }
            }
        }
    }

    private fun initializeFilterView(context: Context?) {
        context?.let {
            val layoutInflater = LayoutInflater.from(context)
            moviefilterView = layoutInflater.inflate(R.layout.movie_filters, filterContentFrame, false) as ConstraintLayout
            if (initialFilterMap?.containsKey(RELEASE_DATE_LTE) == true) {
                (moviefilterView?.findViewById(R.id.releaseDateLte) as TextView).text = initialFilterMap?.get(RELEASE_DATE_LTE) as String
            }
            if (initialFilterMap?.containsKey(RELEASE_DATE_GTE) == true) {
                (moviefilterView?.findViewById(R.id.releaseDateGte) as TextView).text = initialFilterMap?.get(RELEASE_DATE_GTE) as String
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
    interface OnFiltersChangedListener {
        fun onFiltersChanged(map: HashMap<String, String>)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FilterFragment.
         */
        @JvmStatic
        fun newInstance(filters: Map<String, String>) = FilterFragment().apply {
            arguments = Bundle().apply {
                filters.forEach { key, value -> this.putString(key, value) }
            }
        }

    }
}
