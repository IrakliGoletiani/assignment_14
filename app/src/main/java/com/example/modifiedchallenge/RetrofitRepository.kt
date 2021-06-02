package com.example.modifiedchallenge

import retrofit2.Response
import retrofit2.http.GET

interface RetrofitRepository {

    @GET("news")
    suspend fun getNews(): Response<List<NewsModel>>

}