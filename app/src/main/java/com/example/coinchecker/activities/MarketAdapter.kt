package com.example.coinchecker.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import com.example.coinchecker.apiManager.BASE_URL_IMAGE
import com.example.coinchecker.apiManager.model.CoinsData
import com.example.coinchecker.databinding.ItemRecyclerMarketBinding

class MarketAdapter(
    private val data: ArrayList<CoinsData.Data>,
    private val recyclerCallBack: RecyclerCallBack
) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindViews(dataCoin: CoinsData.Data) {

            binding.txtCoinName.text = dataCoin.coinInfo.fullName

           if ("${dataCoin.rAW.uSD.pRICE}".length >= 8) {
                binding.txtPrice.text = "$${dataCoin.rAW.uSD.pRICE}".substring(0,8)
            } else {
               binding.txtPrice.text = "$${dataCoin.rAW.uSD.pRICE}"
            }

            val taghir = dataCoin.rAW.uSD.cHANGEPCT24HOUR

            if (taghir > 0) {

                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.example.coinchecker.R.color.colorGain
                    )
                )

                binding.txtTaghir.text= "${dataCoin.rAW.uSD.cHANGEPCT24HOUR}".substring(0,4) + "%"


            } else if (taghir < 0) {

                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        com.example.coinchecker.R.color.colorLoss
                    )
                )

                binding.txtTaghir.text = "${dataCoin.rAW.uSD.cHANGEPCT24HOUR}".substring(0,5) + "%"

            } else if (taghir == 0.0){
                binding.txtTaghir.text = "0%"
            }

            val marketCap = (dataCoin.rAW.uSD.mKTCAP / 1000000000).toString()
            val index = marketCap.indexOf('.')
            binding.txtMarketCap.text = "$" + marketCap.substring(0,index+3) + " B"


            //    "% ${dataCoin.rAW.uSD.cHANGEPCT24HOUR}"

            Glide
                .with(binding.root)
                .load("$BASE_URL_IMAGE${dataCoin.coinInfo.imageUrl}")
                .into(binding.imgItem)

            itemView.setOnClickListener {
                recyclerCallBack.onCoinItemClicked(dataCoin)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(layoutInflater, parent, false)

        return MarketViewHolder(binding.root)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {

        holder.bindViews(data[position])
    }

    interface RecyclerCallBack {

        fun onCoinItemClicked(dataCoin: CoinsData.Data)

    }
}
