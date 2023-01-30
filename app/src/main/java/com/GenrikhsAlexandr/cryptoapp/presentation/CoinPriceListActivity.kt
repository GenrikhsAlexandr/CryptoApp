package com.genrikhsalexandr.cryptoapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.GenrikhsAlexandr.CriptoApp.databinding.ActivityCoinPriceListBinding
import com.genrikhsalexandr.cryptoapp.pojo.CoinPriceInfo
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CoinPriceListActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinPriceListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCoinPriceListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                val intent = CoinDetailActivity.newIntent(
                    this@CoinPriceListActivity, coinPriceInfo.fromSymbol
                )
                startActivity(intent)
            }
        }
        binding.rvCoinPriceList.adapter = adapter
        viewModel = ViewModelProvider(this)[CoinViewModel::class.java]

        lifecycleScope.launch {
            viewModel.priceList.collect {
                adapter.submitData(it)
            }
        }
    }
}
