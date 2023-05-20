package com.example.coinchecker.activities.coinActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.coinchecker.R
import com.example.coinchecker.apiManager.ALL
import com.example.coinchecker.apiManager.ApiCallback
import com.example.coinchecker.apiManager.ApiManager
import com.example.coinchecker.apiManager.BASE_TWITTER_URL
import com.example.coinchecker.apiManager.HOUR
import com.example.coinchecker.apiManager.HOURS24
import com.example.coinchecker.apiManager.MONTH
import com.example.coinchecker.apiManager.MONTH3
import com.example.coinchecker.apiManager.WEEK
import com.example.coinchecker.apiManager.YEAR
import com.example.coinchecker.apiManager.model.ChartData
import com.example.coinchecker.apiManager.model.CoinAboutItem
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.databinding.ActivityCoinBinding

class CoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsData.Data
    private lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()

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

    // Initialize ui from api for coin activity
    private fun initUi() {

        initChartUi()
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

        var period: String = HOUR
        requestAndShowChart(period)

        binding.layoutChart.radioGroupeMain.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radio1Hour -> {
                    period = HOUR
                    binding.layoutChart.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
                    binding.layoutChart.txtChartChangePercent.text =
                        "${dataThisCoin.dISPLAY.uSD.cHANGEPCT24HOUR}%"
                    binding.layoutChart.txtChartChangePrice.text =
                        dataThisCoin.dISPLAY.uSD.cHANGE24HOUR

                    val changeHours24 = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR

                    if (changeHours24 > 0) {

                        binding.layoutChart.txtChartChangePercent.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorGain
                            )
                        )

                        binding.layoutChart.txtChartUpDown.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorGain
                            )
                        )

                        binding.layoutChart.txtChartUpDown.text = "▲"

                        binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorGain
                        )

                    } else if (changeHours24 < 0) {

                        binding.layoutChart.txtChartChangePercent.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorLoss
                            )
                        )

                        binding.layoutChart.txtChartUpDown.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorLoss
                            )
                        )
                        binding.layoutChart.txtChartUpDown.text = "▼"

                        binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorLoss
                        )


                    } else if (changeHours24 == 0.0) {
                        binding.layoutChart.txtChartChangePercent.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.tertiaryTextColor
                            )
                        )

                        binding.layoutChart.txtChartUpDown.text = ""

                        binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                            binding.root.context,
                            R.color.tertiaryTextColor
                        )

                    }
                }

                R.id.radio1Day -> {
                    period = HOURS24
                    binding.layoutChart.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
                    binding.layoutChart.txtChartChangePercent.text =
                        "${dataThisCoin.dISPLAY.uSD.cHANGEPCTDAY}%"
                    binding.layoutChart.txtChartChangePrice.text =
                        dataThisCoin.dISPLAY.uSD.cHANGEDAY

                    val changeDay = dataThisCoin.rAW.uSD.cHANGEPCTDAY

                    if (changeDay > 0) {

                        binding.layoutChart.txtChartChangePercent.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorGain
                            )
                        )

                        binding.layoutChart.txtChartUpDown.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorGain
                            )
                        )

                        binding.layoutChart.txtChartUpDown.text = "▲"

                        binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorGain
                        )

                    } else if (changeDay < 0) {

                        binding.layoutChart.txtChartChangePercent.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorLoss
                            )
                        )

                        binding.layoutChart.txtChartUpDown.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.colorLoss
                            )
                        )
                        binding.layoutChart.txtChartUpDown.text = "▼"

                        binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorLoss
                        )


                    } else if (changeDay == 0.0) {
                        binding.layoutChart.txtChartChangePercent.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.tertiaryTextColor
                            )
                        )

                        binding.layoutChart.txtChartUpDown.text = ""

                        binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                            binding.root.context,
                            R.color.tertiaryTextColor
                        )
                    }
                }

                R.id.radio1Week -> {
                    period = WEEK
                }

                R.id.radio1Month -> {
                    period = MONTH
                }

                R.id.radio3Month -> {
                    period = MONTH3
                }

                R.id.radio1Year -> {
                    period = YEAR
                }

                R.id.radioAll -> {
                    period = ALL
                }
            }
            requestAndShowChart(period)
        }

        binding.layoutChart.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
        binding.layoutChart.txtChartChangePercent.text =
            "${dataThisCoin.dISPLAY.uSD.cHANGEPCT24HOUR}%"
        binding.layoutChart.txtChartChangePrice.text = dataThisCoin.dISPLAY.uSD.cHANGE24HOUR


        val changeHours24 = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR

        if (changeHours24 > 0) {

            binding.layoutChart.txtChartChangePercent.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.txtChartUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.txtChartUpDown.text = "▲"

            binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorGain
            )

        } else if (changeHours24 < 0) {

            binding.layoutChart.txtChartChangePercent.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.txtChartUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )
            binding.layoutChart.txtChartUpDown.text = "▼"

            binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorLoss
            )


        } else if (changeHours24 == 0.0) {
            binding.layoutChart.txtChartChangePercent.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tertiaryTextColor
                )
            )

            binding.layoutChart.txtChartUpDown.text = ""

            binding.layoutChart.sparkViewChart.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.tertiaryTextColor
            )
        }



        binding.layoutChart.sparkViewChart.setScrubListener {

            if (it == null) {
                binding.layoutChart.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
            } else {
                binding.layoutChart.txtChartPrice.text = "$${(it as ChartData.Data).close}"
            }

        }


    }

    private fun requestAndShowChart(period: String) {

        apiManager.getChartData(
            dataThisCoin.coinInfo.name,
            period,
            object : ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {

                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())
                    binding.layoutChart.sparkViewChart.adapter = chartAdapter

                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(this@CoinActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }
}