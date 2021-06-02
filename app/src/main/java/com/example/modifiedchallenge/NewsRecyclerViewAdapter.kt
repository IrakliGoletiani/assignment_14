package com.example.modifiedchallenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.modifiedchallenge.databinding.ItemNewsRecyclerviewLayoutBinding
import com.example.modifiedchallenge.databinding.ItemRecyclerviewLoadMoreLayoutBinding

class NewsRecyclerViewAdapter(
    private val recyclerView: RecyclerView,
    val news: MutableList<NewsModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_WALL_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var isLoading = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 5
    private var screenHeight = 0

    init {
        screenHeight = Tools.getScreenDimenss().y
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener!!.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WALL_ITEM -> WallPostsViewHolder(
                ItemNewsRecyclerviewLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> LoadMorePostsViewHolder(
                ItemRecyclerviewLoadMoreLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallPostsViewHolder -> holder.onBind()
        }
    }

    inner class WallPostsViewHolder(private val binding: ItemNewsRecyclerviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var model: NewsModel

        fun onBind() {
            model = news[adapterPosition]

            Glide.with(binding.newsCoverImageView.context).load(model.cover)
                .into(binding.newsCoverImageView)
            binding.titleTextView.text = model.titleKA
        }
    }

    inner class LoadMorePostsViewHolder(binding: ItemRecyclerviewLoadMoreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener
    }

    fun setLoaded() {
        isLoading = false
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            news[position].isLast -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_WALL_ITEM
        }
    }
}