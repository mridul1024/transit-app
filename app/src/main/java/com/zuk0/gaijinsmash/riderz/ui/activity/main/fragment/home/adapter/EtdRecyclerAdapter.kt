package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.databinding.ListRowEtdBinding
import com.zuk0.gaijinsmash.riderz.utils.TimeDateUtils

class EtdRecyclerAdapter(val list: MutableList<Etd>) : RecyclerView.Adapter<EtdRecyclerAdapter.EtdViewHolder>() {

    lateinit var binding: ListRowEtdBinding

    //for each ETD, inflate a viewholder with a horizontal recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtdViewHolder {
        binding = ListRowEtdBinding.inflate(LayoutInflater.from(parent.context))
        return EtdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EtdViewHolder, position: Int) {
        val etd = list[position]
        val context = holder.binding.root.context
        holder.binding.estimateResultRecyclerView.setHasFixedSize(true)
        holder.binding.estimateResultRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        holder.binding.estimateResultRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        holder.binding.estimateResultRecyclerView.adapter = EstimateRecyclerAdapter(list[position].estimateList ?: mutableListOf())

        //set last stop
        holder.binding.etdDestinationTv2.text = etd.destination
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class EtdViewHolder(val binding: ListRowEtdBinding): RecyclerView.ViewHolder(binding.root) {
    }

    companion object {
        private const val TAG = "EtdRecyclerAdapter"
    }
}