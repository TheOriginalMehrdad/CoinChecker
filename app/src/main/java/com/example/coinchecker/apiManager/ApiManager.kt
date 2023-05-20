// Initializing api manager to use

package com.example.coinchecker.apiManager

import com.example.coinchecker.apiManager.model.ChartData
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.apiManager.model.NewsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class ApiManager {

    private val apiService: ApiService

    // Initialize retrofit library
    init {

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        apiService = retrofit.create(ApiService::class.java)
    }


    // Get news from api for market activity
    fun getNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {

        apiService.getTopNews().enqueue(object : Callback<NewsData> {

            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {

                val data = response.body()!!

                val dataToSend: ArrayList<Pair<String, String>> = arrayListOf()

                data.data.forEach {
                    dataToSend.add(Pair(it.title, it.url))
                }

                apiCallback.onSuccess(dataToSend)

            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }

        })
    }


    // Get coins data from api for market activity
    fun getCoinsList(apiCallback: ApiCallback<List<CoinsData.Data>>) {

        apiService.getTopCoins().enqueue(object : Callback<CoinsData> {

            override fun onResponse(call: Call<CoinsData>, response: Response<CoinsData>) {

                val data = response.body()!!
                apiCallback.onSuccess(data.data)
            }

            override fun onFailure(call: Call<CoinsData>, t: Throwable) {

                apiCallback.onError(t.message!!)
            }

        })
    }


    // Get data from api for chart
    fun getChartData(
        symbol: String,
        period: String,
        apiCallback: ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>>
    ) {

//        HISTO_MINUTE = "histominute"
//        HISTO_HOUR = "histohour"
//        HISTO_DAY = "histoday"
//        HOUR = "12 hour"
//        HOURS24 = "1 day"
//        WEEK = "1 week"
//        MONTH = "1 month"
//        MONTH3 = "3 month"
//        YEAR = "1 year"
//        ALL = "All"

        var histoPeriod: String = ""
        var limit = 30
        var aggregate = 1

        when (period) {

            HOUR -> {

                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }

            HOURS24 -> {

                histoPeriod = HISTO_HOUR
                limit = 24
            }

            WEEK -> {

                histoPeriod = HISTO_DAY
                aggregate = 7
            }

            MONTH -> {

                histoPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {

                histoPeriod = HISTO_DAY
                limit = 90
            }

            YEAR -> {

                histoPeriod = HISTO_DAY
                limit = 365
                aggregate = 13
            }

            ALL -> {

                histoPeriod = HISTO_DAY
                limit = 2000
                aggregate = 30
            }


        }

        apiService
            .getChartData(histoPeriod, symbol, limit, aggregate)
            .enqueue(object : Callback<ChartData> {

                override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {

                    val fullData = response.body()!!
                    val data1 = fullData.data
                    val data2 = fullData.data.maxByOrNull { it.close.toFloat() }
                    val returningData = Pair(data1, data2)

                    apiCallback.onSuccess(returningData)
                }

                override fun onFailure(call: Call<ChartData>, t: Throwable) {
                    apiCallback.onError(t.message!!)
                }

            })

    }


}

interface ApiCallback<T> {

    fun onSuccess(data: T)
    fun onError(errorMessage: String)
}