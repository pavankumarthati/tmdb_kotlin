package com.careem.tmdb.careemapp.views

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatRadioButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.careem.tmdb.careemapp.R
import com.careem.tmdb.careemapp.common.formatSimpleDateToString
import com.careem.tmdb.careemapp.common.formatStringToSimpleDate
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
const val RELEASE_DATE_GTE = "release_date.gte"
const val RELEASE_DATE_LTE = "release_date.lte"


class FilterFragment : Fragment() {

    private var filterMap: HashMap<String, String>? = null
    private var resetFilterMap: HashMap<String, String>? = null
    private var listener: OnFiltersChangedListener? = null

    private lateinit var sortLabel: TextView
    private lateinit var filterLabel: TextView
    private lateinit var confirmBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var filterContentFrame: FrameLayout
    private var sortByView: RadioGroup? = null
    private var movieFilterView: ConstraintLayout? = null
    private var releaseDateLte: TextView? = null
    private var releaseDateGte: TextView? = null


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
        populateData(arguments!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return initView(inflater, container)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) : View {
        val rootView: View = inflater.inflate(R.layout.fragment_filter, container, false)
        sortLabel = rootView.findViewById(R.id.sort_by_label)
        filterLabel = rootView.findViewById(R.id.filter_by_label)
        confirmBtn = rootView.findViewById(R.id.confirm_btn)
        resetBtn = rootView.findViewById(R.id.reset_btn)
        filterContentFrame = rootView.findViewById(R.id.filter_content)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSortView(context)
        initializeFilterView(context)
        filterContentFrame.addView(sortByView)
        sortLabel.isSelected = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSortAndFilterLabels()
        setupListenersOnFilters()
        setupConfirmAndResetBtns()
    }

    private fun setupConfirmAndResetBtns() {
        confirmBtn.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                updateFilterMap()
            }
        })

        resetBtn.setOnClickListener {
            resetAllOptions()
        }
    }

    private fun resetAllOptions() {
        filterMap = resetFilterMap
        sortByView?.clearCheck()
        when(filterMap?.get(SORT_BY)) {
            RELEASE_DATE_ASC -> {
                (sortByView?.findViewById(R.id.releaseDateAsc) as AppCompatRadioButton).isChecked = true
            }
            RELEASE_DATE_DESC -> {
                (sortByView?.findViewById(R.id.releaseDateDesc) as AppCompatRadioButton).isChecked = true
            }
        }

        (movieFilterView?.findViewById(R.id.releaseDateLte) as TextView).text = filterMap?.get(RELEASE_DATE_LTE)
        (movieFilterView?.findViewById(R.id.releaseDateGte) as TextView).text = filterMap?.get(RELEASE_DATE_GTE)
    }

    private fun setupListenersOnFilters() {
        releaseDateGte?.setOnClickListener { listener?.showDatePicker(SHOW_DATE_PICKER_GTE) }
        releaseDateLte?.setOnClickListener { listener?.showDatePicker(SHOW_DATE_PICKER_LTE) }
    }

    private fun setupSortAndFilterLabels() {
        sortLabel.setOnClickListener { v ->
            toggleLabelAndAddViewIntoPlaceHolder(v)
        }

        filterLabel.setOnClickListener { v ->
            toggleLabelAndAddViewIntoPlaceHolder(v)
        }
    }

    private fun toggleLabelAndAddViewIntoPlaceHolder(v: View) {
        when(v.isSelected) {
            false -> {
                if (v.id == R.id.sort_by_label) {
                    filterContentFrame.removeAllViews()
                    filterLabel.isSelected = false
                    v.isSelected = true
                    filterContentFrame.addView(sortByView)
                } else if (v.id == R.id.filter_by_label) {
                    filterContentFrame.removeAllViews()
                    sortLabel.isSelected = false
                    v.isSelected = true
                    filterContentFrame.addView(movieFilterView)
                }
            }
        }
    }

    private fun populateData(input: Bundle): Unit {
        resetFilterMap = HashMap<String, String>().apply {
            input.keySet().forEach {
                this[it] = input.get(it) as String
            }
        }
        if (filterMap == null) {
            filterMap = resetFilterMap
        }
    }

    private fun updateFilterMap() {
        sortByView?.let {
            when(it.checkedRadioButtonId) {
                R.id.releaseDateDesc -> {
                    filterMap?.put(SORT_BY, RELEASE_DATE_DESC)
                }
                else -> {
                    filterMap?.put(SORT_BY, RELEASE_DATE_ASC)
                }
            }
        }

        movieFilterView?.let {

            if (releaseDateLte?.text?.isNotEmpty() == true) {
                filterMap?.put(RELEASE_DATE_LTE, releaseDateLte?.text.toString())
            }
            if (releaseDateGte?.text?.isNotEmpty() == true) {
                filterMap?.put(RELEASE_DATE_GTE, releaseDateGte?.text.toString())
            }


        }
        listener?.onFiltersChanged(filterMap!!)
    }

    private fun initializeSortView(context: Context?) {
        context?.let {
            val layoutInflater = LayoutInflater.from(context)
            sortByView = layoutInflater.inflate(R.layout.sort_filters, filterContentFrame, false) as RadioGroup
            when(filterMap?.get(SORT_BY)) {
                RELEASE_DATE_DESC -> {
                    (sortByView!!.findViewById(R.id.releaseDateDesc) as AppCompatRadioButton).isChecked = true
                }
                RELEASE_DATE_ASC -> {
                    ((sortByView!!).findViewById(R.id.releaseDateAsc) as AppCompatRadioButton).isChecked = true
                }
                else -> {
                    (sortByView!!.findViewById(R.id.releaseDateDesc) as AppCompatRadioButton).isChecked = true
                    filterMap?.set(RELEASE_DATE_DESC, formatSimpleDateToString(Date()))
                }
            }
        }
    }

    private fun initializeFilterView(context: Context?) {
        context?.let {
            val layoutInflater = LayoutInflater.from(context)
            movieFilterView = layoutInflater.inflate(R.layout.movie_filters, filterContentFrame, false) as ConstraintLayout
            releaseDateLte = movieFilterView?.findViewById(R.id.releaseDateLte) as TextView
            releaseDateGte = movieFilterView?.findViewById(R.id.releaseDateGte) as TextView
            if (filterMap?.containsKey(RELEASE_DATE_LTE) == true) {
                val dt: String = filterMap?.get(RELEASE_DATE_LTE) as String
                (releaseDateLte as TextView).text = dt
                (releaseDateLte as TextView).tag = formatStringToSimpleDate(dt)
            }
            if (filterMap?.containsKey(RELEASE_DATE_GTE) == true) {
                val dt: String = filterMap?.get(RELEASE_DATE_LTE) as String
                (releaseDateGte as TextView).text = filterMap?.get(RELEASE_DATE_GTE) as String
                (releaseDateGte as TextView).tag = formatStringToSimpleDate(dt)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun onDateSelected(item_type: String, cal: Calendar): Unit {
        when (item_type) {
            RELEASE_DATE_GTE -> {
                if (validateDates(cal.time, releaseDateLte?.tag as Date?, context?.getString(R.string.error_msg_date_validate))) {
                    releaseDateGte?.text = formatSimpleDateToString(cal.time)
                    releaseDateGte?.tag = cal.time
                }
            }
            RELEASE_DATE_LTE -> {
                if (validateDates(releaseDateGte?.tag as Date?, cal.time, context?.getString(R.string.error_msg_date_validate))) {
                    releaseDateLte?.text = formatSimpleDateToString(cal.time)
                    releaseDateLte?.tag = cal.time
                }
            }
        }
    }

    private fun validateDates(gteDate: Date?, lteDate: Date?, errorMsg: String?) : Boolean {
        if (gteDate == null || lteDate == null) return true

        if (gteDate.after(lteDate) || gteDate == lteDate) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    interface OnFiltersChangedListener {
        fun onFiltersChanged(map: HashMap<String, String>)
        fun showDatePicker(id: Int)
    }

    companion object {

        @JvmStatic
        fun newInstance(filters: Map<String, String>) = FilterFragment().apply {
            arguments = Bundle().apply {
                filters.forEach { key, value -> this.putString(key, value) }
            }
        }

    }
}
