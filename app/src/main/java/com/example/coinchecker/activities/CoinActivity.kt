package com.example.coinchecker.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coinchecker.apiManager.BASE_TWITTER_URL
import com.example.coinchecker.apiManager.model.CoinAboutItem
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.apiManager.model.NewsData
import com.example.coinchecker.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsData.Data
    private lateinit var dataThisCoinAbout: CoinAboutItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fromIntent = intent.getBundleExtra("bundle")!!
        dataThisCoin = fromIntent.getParcelable<CoinsData.Data>("bundle1")!!


        if (fromIntent.getParcelable<CoinAboutItem>("bundle2") != null) {
            dataThisCoinAbout = fromIntent.getParcelable<CoinAboutItem>("bundle2")!!
        } else {
            dataThisCoinAbout = CoinAboutItem()
        }



        binding.layoutToolbar.toolBar.title = dataThisCoin.coinInfo.fullName


        initUi()
    }

    private fun initUi() {
        // initChartUi()
        initAboutUi()
        initStatisticsUi()

    }

    @SuppressLint("SetTextI18n")
    private fun initAboutUi() {

        binding.layoutAbout.txtWebsite.text = dataThisCoinAbout.coinWebsite
        binding.layoutAbout.txtGithub.text = dataThisCoinAbout.coinGithub
        binding.layoutAbout.txtReddit.text = dataThisCoinAbout.coinReddit
        binding.layoutAbout.txtTwitter.text = "@${dataThisCoinAbout.coinTwitter}"
        binding.layoutAbout.txtAboutCoin.text = dataThisCoinAbout.coinDesc

        binding.layoutAbout.txtWebsite.setOnClickListener {
            openWebsiteCoin(dataThisCoinAbout.coinWebsite!!)
        }

        binding.layoutAbout.txtGithub.setOnClickListener {
            openWebsiteCoin(dataThisCoinAbout.coinGithub!!)
        }

        binding.layoutAbout.txtReddit.setOnClickListener {
            openWebsiteCoin(dataThisCoinAbout.coinReddit!!)
        }

        binding.layoutAbout.txtTwitter.setOnClickListener {
            openWebsiteCoin("$BASE_TWITTER_URL${dataThisCoinAbout.coinTwitter!!}")
        }

    }
    /// just test

    private fun openWebsiteCoin(url: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun initStatisticsUi() {

        binding.layoutStatistics.tvOpenAmount.text = dataThisCoin.dISPLAY.uSD.oPEN24HOUR
        binding.layoutStatistics.tvTodaysHighAmount.text = dataThisCoin.dISPLAY.uSD.hIGH24HOUR
        binding.layoutStatistics.tvTodayLowAmount.text = dataThisCoin.dISPLAY.uSD.lOW24HOUR
        binding.layoutStatistics.tvChangeTodayAmount.text = dataThisCoin.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutStatistics.tvAlgorithm.text = dataThisCoin.coinInfo.algorithm
        binding.layoutStatistics.tvTotalVolume.text = dataThisCoin.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStatistics.tvAvgMarketCapAmount.text = dataThisCoin.dISPLAY.uSD.mKTCAP
        binding.layoutStatistics.tvSupplyNumber.text = dataThisCoin.dISPLAY.uSD.sUPPLY
    }

    private fun initChartUi() {
        TODO("Not yet implemented")
    }
}