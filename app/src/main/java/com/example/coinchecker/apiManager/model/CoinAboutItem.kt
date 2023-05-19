package com.example.coinchecker.apiManager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinAboutItem(
    var coinWebsite: String? = "No-Data!",
    var coinGithub: String? = "No-Data!",
    var coinTwitter: String? = "No-Data",
    var coinDesc: String? = "No-Data!",
    var coinReddit: String? = "No-Data!"
) : Parcelable