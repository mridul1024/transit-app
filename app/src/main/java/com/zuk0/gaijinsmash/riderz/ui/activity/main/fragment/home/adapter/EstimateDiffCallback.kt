package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Estimate

class EstimateDiffCallback(private val oldList: List<Estimate>, private val newList: List<Estimate>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].destination == newList[newItemPosition].destination
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}