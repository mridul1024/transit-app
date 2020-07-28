package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter.etd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import com.zuk0.gaijinsmash.riderz.databinding.ListRowPlatformBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel

class PlatformReyclerAdapter(val list: MutableList<MutableList<Etd>?>?, val viewModel: HomeViewModel): RecyclerView.Adapter<PlatformReyclerAdapter.PlatformViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformViewHolder {
        val binding = ListRowPlatformBinding.inflate(LayoutInflater.from(parent.context))
        return PlatformViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        if(itemCount == 0) {
            holder.binding.root.visibility = View.GONE
            return
        }
        val etdList = list?.get(position) ?: mutableListOf()
        val title = "${holder.binding.root.context.resources.getString(R.string.platform)} ${etdList[0].estimateList?.get(0)?.platform}"
        holder.binding.platformTitle.text = title
        holder.binding.platformRv.setHasFixedSize(true)
        holder.binding.platformRv.isNestedScrollingEnabled = false
        holder.binding.platformRv.layoutManager = LinearLayoutManager(holder.binding.root.context, RecyclerView.VERTICAL, false)
        holder.binding.platformRv.adapter = EtdRecyclerAdapter(etdList, viewModel)
    }

    inner class PlatformViewHolder(val binding: ListRowPlatformBinding): RecyclerView.ViewHolder(binding.root)

}

