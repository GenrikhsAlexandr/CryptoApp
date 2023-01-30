package com.genrikhsalexandr.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.genrikhsalexandr.cryptoapp.api.ApiFactory
import com.genrikhsalexandr.cryptoapp.database.AppDatabase
import com.genrikhsalexandr.cryptoapp.pojo.CoinPriceInfo
import com.genrikhsalexandr.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val api = ApiFactory.apiService
    private val db = AppDatabase.getInstance(application)
    val priceList = db.coinPriceInfoDao().getPriceList()

    fun getDetailInfo(fSym: String): Flow<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    init {
        startLoadingJob()
    }

    private fun startLoadingJob() {
        viewModelScope.launch() {
            try {
                while (true) {
                    loadData()
                    delay(10_000)
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    private suspend fun onError() {
        delay(10_000)
        startLoadingJob()
    }

    private suspend fun loadData() = withContext(Dispatchers.IO) {
        val coinInfo = api.getTopCoinsInfo().let { response ->
            response.data?.joinToString(",") { it.coinInfo?.name.toString() }.toString()
        }
        val priceList = api.getFullPriceList(fSyms = coinInfo)
        val list = getPriceListFromRawData(priceList)
        db.coinPriceInfoDao().insertPriceList(list)
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData,
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }
}
