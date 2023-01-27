package com.genrikhsalexandr.cryptoapp.pojo

import com.genrikhsalexandr.cryptoapp.pojo.CoinInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Datum(
    @SerializedName("CoinInfo") @Expose val coinInfo: CoinInfo? = null,
)