package com.example.modifiedchallenge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.modifiedchallenge.databinding.NewsFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels()

    lateinit var binding: NewsFragmentBinding

    private lateinit var newsRecyclerViewAdapter: NewsRecyclerViewAdapter

    private val news = mutableListOf<NewsModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsFragmentBinding.inflate(layoutInflater)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getNews()
        }

        init()

        viewModel._newsLiveData.observe(viewLifecycleOwner, {
            loadNews(it)
        })

        return binding.root
    }

    private fun init() {
        binding.newsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        newsRecyclerViewAdapter = NewsRecyclerViewAdapter(binding.newsRecyclerView, news)

        newsRecyclerViewAdapter.setOnLoadMoreListener(loadMoreListener)

        binding.newsRecyclerView.adapter = newsRecyclerViewAdapter
    }

    private val loadMoreListener = object : OnLoadMoreListener {
        override fun onLoadMore() {
            if (news.size != 0) {
                if (!news[news.size - 1].isLast) {
                    binding.newsRecyclerView.post {
                        val newsModel = NewsModel()
                        newsModel.isLast = true
                        news.add(newsModel)
                        newsRecyclerViewAdapter.notifyItemInserted(news.size - 1)

                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getNews()
                        }
                    }
                }
            }
        }
    }

    private fun loadNews(newsList: List<NewsModel>) {
        binding.progressBar.visibility = View.GONE

        val lastPosition = news.size

        if (lastPosition > 0) {
            news.removeAt(news.size - 1)
            newsRecyclerViewAdapter.notifyItemRemoved(news.size)
        }

        newsRecyclerViewAdapter.setLoaded()

        news.addAll(newsList)

        if (news.isNotEmpty() && news.size != 1) newsRecyclerViewAdapter.notifyItemMoved(
            lastPosition,
            news.size - 1
        ) else newsRecyclerViewAdapter.notifyDataSetChanged()
    }
}