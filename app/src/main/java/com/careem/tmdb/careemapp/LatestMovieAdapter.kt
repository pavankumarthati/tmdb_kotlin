package com.careem.tmdb.careemapp

import android.arch.paging.PagedListAdapter
import android.graphics.Bitmap
import android.provider.SyncStateContract
import android.support.v7.graphics.Palette
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget

class LatestMovieAdapter(diffCallback: DiffCallback<LatestMovieItem>) : PagedListAdapter<LatestMovieItem, RecyclerView.ViewHolder>(diffCallback) {

    var loadingStatus: LoadingStatus? = null
            set(newLoadingStatus) {
                val previousState : LoadingStatus? = field;
                val previousExtraRow : Boolean = hasExtraRow();
                field = newLoadingStatus;
                val newExtraRow : Boolean = hasExtraRow();
                if (previousExtraRow != newExtraRow) {
                    if (previousExtraRow) {
                        notifyItemRemoved(itemCount);
                    } else {
                        notifyItemInserted(itemCount);
                    }
                } else if (newExtraRow && previousState != newLoadingStatus) {
                    notifyItemChanged(itemCount - 1);
                }
            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == R.layout.content_loading_item) {
            val view = inflater.inflate(viewType, parent, false)
            return ContentLoadingHolder(view)
        } else if (viewType == R.layout.latest_movie_item) {
            val view = inflater.inflate(viewType, parent, false)
            return LatestMovieHolder(view)
        } else {
            throw IllegalArgumentException("no view matched to viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType: Int = getItemViewType(position)
        when {
            viewType == R.layout.content_loading_item && holder is ContentLoadingHolder -> {
                holder.errorMsg.text = loadingStatus?.reason
                holder.progressBar.visibility = View.VISIBLE
            }
            viewType == R.layout.latest_movie_item && holder is LatestMovieHolder -> {
                val item : LatestMovieItem? = getItem(position)
                holder.apply {
                    itemView.tag = item
                    title.text = item?.title
                    overview.text = item?.overview
                    votes.text = votes.context.resources.getString(R.string.vote_count, item?.votes)
                    popularity.text = popularity.context.resources.getString(R.string.popularity_count, item?.popularity)

                    Glide.with(img.context.applicationContext)
                            .load(POSTER_PATH + item?.posterPath)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(object : BitmapImageViewTarget(img) {
                                override fun onResourceReady(bitmap: Bitmap?, anim: GlideAnimation<in Bitmap>?) {
                                    super.onResourceReady(bitmap, anim)
                                    bitmap?.let {
                                        Palette.from(it).generate(object: Palette.PaletteAsyncListener {
                                            override fun onGenerated(palette: Palette) {
                                                img.setBackgroundColor(palette.getLightVibrantColor(img.context!!.resources.getColor(R.color.black_translucent_60)))
                                            }

                                        })
                                    }
                                }
                            })
                }
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        when (holder) {
            is LatestMovieHolder -> Glide.clear(holder.img)
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (hasExtraRow() && itemCount - 1 == position) {
            R.layout.content_loading_item
        } else {
            R.layout.latest_movie_item
        }

    override fun getItemCount(): Int =
        when {
            hasExtraRow() -> super.getItemCount() + 1
            else -> super.getItemCount()
        }

    private fun hasExtraRow() : Boolean = loadingStatus?.status != Status.LOADED


    class LatestMovieHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        var title : TextView = rootView.findViewById(R.id.title)
        var img: ImageView = rootView.findViewById(R.id.movieImg)
        var overview: TextView = rootView.findViewById(R.id.overview)
        var popularity: TextView = rootView.findViewById(R.id.popularity)
        var votes: TextView = rootView.findViewById(R.id.votes)
    }

    class ContentLoadingHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        var errorMsg: TextView
        var progressBar: ProgressBar

        init {
            errorMsg = rootView.findViewById(R.id.error_msg)
            progressBar = rootView.findViewById(R.id.progress_bar)
        }
    }


}