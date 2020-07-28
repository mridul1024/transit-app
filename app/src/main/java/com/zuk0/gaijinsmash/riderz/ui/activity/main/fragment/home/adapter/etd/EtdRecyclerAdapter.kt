package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.etd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.databinding.ListRowEtdBinding
import com.zuk0.gaijinsmash.riderz.databinding.ListRowEtdEmptyBinding
import com.zuk0.gaijinsmash.riderz.databinding.ListRowEtdTitleBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel

class EtdRecyclerAdapter(val list: MutableList<Etd>, val viewModel: HomeViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isEndOfLine = false

    //for each ETD, inflate a viewholder with a horizontal recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ETD -> {
                val binding = ListRowEtdBinding.inflate(LayoutInflater.from(parent.context))
                EtdViewHolder(binding)
            }
            VIEW_TYPE_END_OF_LINE -> {
                val b = ListRowEtdEmptyBinding.inflate(LayoutInflater.from(parent.context))
                EndOfLineViewHolder(b)
            }
            else -> {
                val binding = ListRowEtdBinding.inflate(LayoutInflater.from(parent.context))
                EtdViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is EtdViewHolder -> {
                val etd = list[position]
                val context = holder.binding.root.context

                //set title view if needed
                holder.binding.estimateResultRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
                holder.binding.estimateResultRecyclerView.setHasFixedSize(true)
                holder.binding.estimateResultRecyclerView.isNestedScrollingEnabled = false
                holder.binding.estimateResultRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                holder.binding.estimateResultRecyclerView.adapter = EstimateRecyclerAdapter(list[position].estimateList
                        ?: mutableListOf(), viewModel)

                //set last stop
                holder.binding.etdDestinationTv2.text = etd.destination
            }
            is EndOfLineViewHolder -> {
                holder.binding.noEtdTv
            }
        }
    }

    override fun getItemCount(): Int {
        if(list.isEmpty()) {
            isEndOfLine = true
            return 1
        }
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if(isEndOfLine)
            return VIEW_TYPE_END_OF_LINE
        return VIEW_TYPE_ETD
    }

    inner class EtdViewHolder(val binding: ListRowEtdBinding): RecyclerView.ViewHolder(binding.root) {
    }

    inner class EndOfLineViewHolder(val binding: ListRowEtdEmptyBinding): RecyclerView.ViewHolder(binding.root) {
    }
    companion object {
        private const val TAG = "EtdRecyclerAdapter"
        private const val VIEW_TYPE_ETD = 1000
        private const val VIEW_TYPE_END_OF_LINE = 2000
        private const val VIEW_TYPE_TITLE = 3000
    }
}