package com.genrikhsalexandr.cryptoapp.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.GenrikhsAlexandr.CriptoApp.R
import com.GenrikhsAlexandr.CriptoApp.databinding.ItemCoinInfoBinding
import com.genrikhsalexandr.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso

class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
    fun submitData(coinInfoList: List<CoinPriceInfo>) {
        this.coinInfoList = coinInfoList
        notifyDataSetChanged()
    }

    var onCoinClickListener: OnCoinClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        return CoinInfoViewHolder(
            ItemCoinInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = coinInfoList.size

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        holder.bind(coinInfoList[position])
    }

    inner class CoinInfoViewHolder(private val binding: ItemCoinInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coinInfo: CoinPriceInfo) {
            with(binding) {
                with(coinInfo) {
                    val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                    val lastUpdate = context.resources.getString(R.string.last_update)
                    tvSymbols.text = String.format(symbolsTemplate, fromSymbol, toSymbol)
                    tvPrice.text = price
                    tvLastUpdate.text = String.format(lastUpdate, getFormattedTime())
                    Picasso.get().load(getFullImageUrl()).into(ivLogoCoin)
                    itemView.setOnClickListener {
                        onCoinClickListener?.onCoinClick(this)
                    }
                }
            }

        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}