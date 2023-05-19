package com.example.coinchecker.apiManager.model


import com.google.gson.annotations.SerializedName

class CoinAboutData2 : ArrayList<CoinAboutData2.CoinAboutData2Item>(){
    data class CoinAboutData2Item(
        @SerializedName("currencyName")
        val currencyName: String?,
        @SerializedName("info")
        val info: Info?
    ) {
        data class Info(
            @SerializedName("desc")
            val desc: String?,
            @SerializedName("forum")
            val forum: String?,
            @SerializedName("github")
            val github: String?,
            @SerializedName("reddit")
            val reddit: String?,
            @SerializedName("twt")
            val twt: String?,
            @SerializedName("web")
            val web: String?
        )
    }
}