package com.careem.tmdb.careemapp.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.careem.tmdb.careemapp.*
import com.careem.tmdb.careemapp.common.formatSimpleDateToString
import com.careem.tmdb.careemapp.common.giveStartOfADay
import java.util.*

const val FILTER_FRAGMENT_TAG: String = "filter_frag_tag"
const val MOVIE_DETAIL_FRAG_TAG = "movie_detail_frag_tag"
const val SHOW_DATE_PICKER_LTE = 222
const val SHOW_DATE_PICKER_GTE = 777

class MainActivity : AppCompatActivity(), MainContentFragment.OnFragmentInteractionListener, FilterFragment.OnFiltersChangedListener {

    lateinit var mainContent: MainContentFragment
    lateinit var filterContent: FilterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFrags();
    }

    private fun setupFrags() {
        val filters = HashMap<String, String>().apply {
            this[SORT_BY] = RELEASE_DATE_DESC
            this[RELEASE_DATE_LTE] = formatSimpleDateToString(Date())
        }

        mainContent = MainContentFragment.newInstance(filters)
        filterContent = FilterFragment.newInstance(filters)
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.content_holder, mainContent)
            this.commit()
        }
    }

    override fun onFilterClicked() {
        addFragToBackStack()
    }

    private fun addFragToBackStack(): Unit {
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.content_holder, filterContent, FILTER_FRAGMENT_TAG)
            this.addToBackStack(null)
            this.commit()
        }
    }

    override fun onFiltersChanged(map: HashMap<String, String>) {
        supportFragmentManager.popBackStack()
        mainContent.applyFilters(map)
    }

    override fun onItemClicked(id: Long) {
        openDetailFrag(id)
    }

    private fun openDetailFrag(id: Long) {
        val movieDetailFragment = MovieDetailFragment.getInstance(id)
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.content_holder, movieDetailFragment, MOVIE_DETAIL_FRAG_TAG)
            this.addToBackStack(null)
            this.commit()
        }
    }

    override fun onCreateDialog(id: Int): Dialog? {
        if (mainContent.isVisible) {
            val cal: Calendar = giveStartOfADay(Calendar.getInstance(TimeZone.getDefault(), Locale.US))
            if (id == SHOW_DATE_PICKER_LTE) {
                return DatePickerDialog(this,
                        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                            run {
                                cal.set(Calendar.YEAR, year)
                                cal.set(Calendar.MONTH, month)
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                filterContent.onDateSelected(RELEASE_DATE_LTE, cal)
                            }
                        },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            } else if (id == SHOW_DATE_PICKER_GTE) {
                return DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    run {
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        filterContent.onDateSelected(RELEASE_DATE_GTE, cal)
                    }
                },
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            }
        }
        return null
    }

    override fun showDatePicker(id: Int) {
        showDialog(id)
    }
}
