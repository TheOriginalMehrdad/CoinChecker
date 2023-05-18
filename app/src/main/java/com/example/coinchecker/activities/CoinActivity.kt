package com.example.coinchecker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.apiManager.model.NewsData
import com.example.coinchecker.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsData.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dataThisCoin = intent.getParcelableExtra<CoinsData.Data>("DataToSend")!!
        binding.layoutToolbar.toolBar.title = dataThisCoin.coinInfo.fullName


        initUi()
    }

    private fun initUi() {
        // initChartUi()
        initStatisticsUi()
        // initAboutUi()
    }

    private fun initAboutUi() {
        TODO("Not yet implemented")
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