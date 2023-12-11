package com.example.earningapp.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.example.earningapp.databinding.HistoryitemBinding
import com.example.earningapp.model.historymodelclass
import com.google.type.Date
import java.sql.Timestamp

class historyadapter(var listHistory:ArrayList<historymodelclass>): RecyclerView.Adapter<historyadapter.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder (var binding: HistoryitemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCoinViewHolder {
        return HistoryCoinViewHolder(HistoryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount()=listHistory.size

    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {
        var timestamp= Timestamp(listHistory[position].timeAndDate.toLong())
        holder.binding.time.text= java.util.Date(timestamp.time).toString()
        holder.binding.status.text=if(listHistory[position].isWithdrawal){"- Money Withdrawal"}
        else{"+ Money Credited"}
        holder.binding.Coin.text=listHistory[position].coin
    }
}