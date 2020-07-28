package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.diff_util.FavoriteDiffCallback
import com.zuk0.gaijinsmash.riderz.databinding.ListRowFavorites2Binding
import com.zuk0.gaijinsmash.riderz.ui.shared.adapter.RecyclerAdapterClickListener

class FavoriteRecyclerAdapter(val mFavoriteList: MutableList<Favorite>, private val mParent: Fragment) : RecyclerView.Adapter<FavoriteViewHolder>(), RecyclerAdapterClickListener {

    private var listener: View.OnClickListener? = null //todo  - use this as general listener for all buttons

    override fun setClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ListRowFavorites2Binding.inflate(LayoutInflater.from(parent.context))
        return FavoriteViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = mFavoriteList[position]
        holder.bind(favorite)
    }

    override fun getItemCount(): Int {
        return mFavoriteList.size
    }

    /**
     * Public Method
     * Diff util - updates the recyclerview asynchronously
     */
    fun updateList(newList: MutableList<Favorite>?) {

        val diffResult = DiffUtil.calculateDiff(FavoriteDiffCallback(this.mFavoriteList, newList ?: mutableListOf()))
        diffResult.dispatchUpdatesTo(this)

        if(itemCount == 0) {
            val card = mParent.activity?.findViewById<CardView>(R.id.bartFavorites_cardView)
            card?.visibility = View.VISIBLE
        }
    }
}
