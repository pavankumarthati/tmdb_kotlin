package com.careem.tmdb.careemapp

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.View
import org.joda.time.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.*

const val FILTER_FRAGMENT_TAG: String = "filter_frag_tag"
const val MOVIE_DETAIL_FRAG_TAG = "movie_detail_frag_tag"

class MainActivity : AppCompatActivity(), MainContentFragment.OnFragmentInteractionListener, FilterFragment.OnFiltersChangedListener {

    var mainContent: MainContentFragment? = null
    var filterContent: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filters = HashMap<String, String>().apply {
            this["sort_by"] =  "release_date.desc"
            this["release_date.lte"] = formatToSimpleDate(Date())
        }

        mainContent = MainContentFragment.newInstance(filters)
        filterContent = FilterFragment.newInstance(filters)
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.content_holder, mainContent)
            this.commit()
        }
    }

    override fun onFilterClicked() {
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.content_holder, filterContent, FILTER_FRAGMENT_TAG)
            this.addToBackStack(null)
            this.commit()
        }
    }

    override fun onFiltersChanged(map: HashMap<String, String>) {
        supportFragmentManager.popBackStack()
        mainContent?.applyFilters(map)
    }

    override fun onItemClicked(id: Long) {
        val movieDetailFragment = MovieDetailFragment.getInstance(id)
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.content_holder, movieDetailFragment, MOVIE_DETAIL_FRAG_TAG)
            this.addToBackStack(null)
            this.commit()
        }
    }

}
