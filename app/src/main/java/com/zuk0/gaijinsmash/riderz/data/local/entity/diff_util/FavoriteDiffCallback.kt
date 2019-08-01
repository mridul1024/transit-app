package com.zuk0.gaijinsmash.riderz.data.local.entity.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite

class FavoriteDiffCallback(private val oldFavorites: MutableList<Favorite>,  private val newFavorites: MutableList<Favorite>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavorites[oldItemPosition].id == newFavorites[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldFavorites.size
    }

    override fun getNewListSize(): Int {
        return newFavorites.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavorites[oldItemPosition] == newFavorites[newItemPosition]
    }

}