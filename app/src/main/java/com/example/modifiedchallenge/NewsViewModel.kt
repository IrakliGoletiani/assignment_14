package com.example.modifiedchallenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

class NewsViewModel : ViewModel() {

    private val newsLiveData =
        MutableLiveData<List<NewsModel>>().apply { mutableListOf<NewsModel>() }
    val _newsLiveData: LiveData<List<NewsModel>> = newsLiveData

    suspend fun getNews() {
        try {
            val result = RetrofitService.retrofitService().getNews()

            if (result.isSuccessful) {
                val news = result.body()
                newsLiveData.postValue(news)
            }
        } catch (e: Exception) {
            Log.d("mainlog", "$e")
        }
    }
}