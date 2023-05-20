package com.example.coinchecker.activities.marketActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinchecker.activities.coinActivity.CoinActivity
import com.example.coinchecker.apiManager.ApiCallback
import com.example.coinchecker.apiManager.ApiManager
import com.example.coinchecker.apiManager.model.CoinAboutData
import com.example.coinchecker.apiManager.model.CoinAboutItem
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.databinding.ActivityMarketBinding
import com.google.gson.Gson

class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallBack {

    lateinit var binding: ActivityMarketBinding
    private lateinit var dataNews: ArrayList<Pair<String, String>>
    lateinit var dataAboutMap: MutableMap<String, CoinAboutItem>
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

        getAboutDataFromAsset()
    }
    override fun onResume() {
        super.onResume()
        initUi()
    }


    // Base Ui for market activity
    private fun initUi() {

        getNewsFromApi()
        getTopCoinsFromApi()
    }

    // Convert json file from api to kotlin class to use for coin activity
    private fun getAboutDataFromAsset() {

        // Convert json from api to kotlin class
        val fileInString = applicationContext.assets.open("currencyinfo.json").bufferedReader().use { it.readText() }


        dataAboutMap = mutableMapOf<String, CoinAboutItem>()
        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileInString, CoinAboutData::class.java)!!
        dataAboutAll.forEach {

            dataAboutMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit
            )
        }

    }

    // For recycler view in activity market
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

    // For news in activity market
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

    // Intent to coin activity from market activity
    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1", dataCoin)
        bundle.putParcelable("bundle2", dataAboutMap[dataCoin.coinInfo.name])
        intent.putExtra("bundle", bundle)
        startActivity(intent)

    }
}



