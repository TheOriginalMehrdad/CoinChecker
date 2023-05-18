package com.example.coinchecker.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinchecker.apiManager.ApiCallback
import com.example.coinchecker.apiManager.ApiManager
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.databinding.ActivityMarketBinding

class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallBack {

    lateinit var binding: ActivityMarketBinding
    private lateinit var dataNews: ArrayList<Pair<String, String>>
    private val apiManager = ApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.layoutToolbar.toolBar.title = "CryptoChecker"


        // Initialize and refresh api in Ui
        onResume()

        // Show more coins in live coin watch
        binding.layoutWatchList.btnShowMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }


        // Refresh information
        binding.swipeRefreshLayout.setOnRefreshListener {
            initUi()


            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding.swipeRefreshLayout.isRefreshing = false

                }, 900
            )
        }
    }

    // Initialize and refresh ui fun
    override fun onResume() {
        super.onResume()
        initUi()
    }

    // Ui page design
    private fun initUi() {

        getNewsFromApi()
        getTopCoinsFromApi()
    }

    private fun getTopCoinsFromApi() {

        apiManager.getCoinsList(object : ApiCallback<List<CoinsData.Data>> {

            override fun onSuccess(data: List<CoinsData.Data>) {
                showDataInRecycler(data)
            }

            override fun onError(errorMessage: String) {

                Toast.makeText(this@MarketActivity, "Error -> $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun showDataInRecycler(data: List<CoinsData.Data>) {

        val marketAdapter = MarketAdapter(ArrayList(data), this)

        binding.layoutWatchList.recyclerMain.adapter = marketAdapter
        binding.layoutWatchList.recyclerMain.layoutManager = LinearLayoutManager(this)
    }

    private fun getNewsFromApi() {

        apiManager.getNews(object : ApiCallback<ArrayList<Pair<String, String>>> {

            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()
            }

            override fun onError(errorMessage: String) {

            }
        })

    }

    private fun refreshNews() {

        val randomAccess = (0..49).random()

        binding.layoutNews.txtNews.text = dataNews[randomAccess].first
        binding.layoutNews.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)
        }
        binding.layoutNews.txtNews.setOnClickListener {
            refreshNews()
        }
    }

    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)
        intent.putExtra("DataToSend", dataCoin)
        startActivity(intent)

    }
}



