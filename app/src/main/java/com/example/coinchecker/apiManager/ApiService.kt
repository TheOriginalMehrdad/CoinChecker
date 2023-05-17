package com.example.coinchecker.apiManager

import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.apiManager.model.NewsData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiService {


    //Api to get news for coin activity
    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrder") sortOrder: String = "popular"
    ): Call<NewsData>


    //Api to get top coins for coin activity
    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTopCoins(
        @Query("tsym") to_symbol: String = "USD",
        @Query("limit") limit_data: Int = 13
    ): Call<CoinsData>

}