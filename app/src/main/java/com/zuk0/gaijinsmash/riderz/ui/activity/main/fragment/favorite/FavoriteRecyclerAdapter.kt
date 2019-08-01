package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.RoomDatabase

import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.diff_util.FavoriteDiffCallback
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.databinding.ListRowFavorites2Binding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment
import com.zuk0.gaijinsmash.riderz.ui.shared.adapter.RecyclerAdapterClickListener

class FavoriteRecyclerAdapter(private val mFavoriteList: MutableList<Favorite>, private val mParent: Fragment) : RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder>(), RecyclerAdapterClickListener {

    private var listener: View.OnClickListener? = null //todo  - use this as general listener for all buttons
    private val db: FavoriteDatabase
    private lateinit var favoritesFromDb: List<Favorite>


    init {
        db = FavoriteDatabase.getRoomDB(mParent.context)
    }

    override fun setClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListRowFavorites2Binding>(LayoutInflater.from(parent.context), R.layout.list_row_favorites2, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = mFavoriteList[position]
        val stationA = StationList.getAbbrFromStationName(favorite.a.name)
        val stationB = StationList.getAbbrFromStationName(favorite.b.name)
        holder.binding.departStation.text = stationA
        holder.binding.arriveStation.text = stationB
        holder.binding.settingsButton.setOnClickListener { v -> initPopupMenu(v.context, holder.binding.settingsButton, favorite, position) }
        if (favorite.priority === Favorite.Priority.ON) {
            holder.binding.background.setBackgroundColor(holder.binding.root.context.resources.getColor(R.color.primaryLightColor))
        }
        holder.binding.switchButton.setOnClickListener { v -> reverseRoute(holder.binding.departStation, holder.binding.arriveStation) }
        holder.binding.getTripButton.setOnClickListener { v -> initTripSearch(mParent, holder.binding.departStation, holder.binding.arriveStation) }
    }

    override fun getItemCount(): Int {
        return mFavoriteList.size
    }

    private fun initPopupMenu(context: Context, ib: ImageButton, favorite: Favorite?, rowPosition: Int) {
        val popupMenu = PopupMenu(context, ib)
        popupMenu.menuInflater.inflate(R.menu.favorite_options, popupMenu.menu)
        popupMenu.show()
        if (favorite != null) {
            if (favorite.priority === Favorite.Priority.ON) {
                Log.i("PRIORITY", "ON")
                val deletePriorityItem = popupMenu.menu.findItem(R.id.action_favorite_deletePriority)
                deletePriorityItem.isVisible = true
            } else {
                val setPriorityItem = popupMenu.menu.findItem(R.id.action_favorite_setPriority)
                setPriorityItem.isVisible = true
            }
        }
        popupMenu.setOnMenuItemClickListener { item ->
            selectFavoriteAction(popupMenu, context, item.itemId, favorite, rowPosition)
            true
        }
    }

    private fun selectFavoriteAction(menu: PopupMenu, context: Context, itemId: Int, favorite: Favorite?, rowPosition: Int) {
        when (itemId) {
            R.id.action_delete_favorite -> {
                FavoritesViewModel.deleteFavorite(context, favorite)
                menu.dismiss()
                Toast.makeText(context, R.string.deletion_success, Toast.LENGTH_SHORT).show()
                this.notifyItemRemoved(rowPosition)
                if (itemCount == 1) {
                    mFavoriteList.clear()
                    notifyDataSetChanged()
                }
            }
            R.id.action_favorite_setPriority -> {
                //todo: prompt user if he/she wants to remove current priority, and make this the priority
                FavoritesViewModel.addPriority(context, favorite)
                menu.dismiss()
                notifyItemChanged(rowPosition)
            }
            R.id.action_favorite_deletePriority -> {
                FavoritesViewModel.removePriority(context, favorite)
                menu.dismiss()
                Toast.makeText(context, R.string.priority_deleted, Toast.LENGTH_SHORT).show()
                notifyItemRemoved(rowPosition)
            }
        }
    }

    /*
        Note: pulls text directly from textviews, which are in Abbreviated format
        Must get full stations names for sql query in favorites database.
    */
    private fun initTripSearch(parent: Fragment, origin: TextView, destination: TextView) {
        val originString = origin.text.toString()
        val destinationString = destination.text.toString()

        val bundle = Bundle()
        bundle.putString(TripFragment.TripBundle.ORIGIN.value, originString)
        bundle.putString(TripFragment.TripBundle.DESTINATION.value, destinationString)
        bundle.putString(TripFragment.TripBundle.DATE.value, "TODAY")
        bundle.putString(TripFragment.TripBundle.TIME.value, "NOW")
        bundle.putBoolean("FAVORITE_RECYCLER_ADAPTER", true)

        NavHostFragment.findNavController(parent).navigate(R.id.action_favoritesFragment_to_resultsFragment, bundle, null, null)
    }

    private fun reverseRoute(origin: TextView, destination: TextView) {
        val temp = origin.text.toString()
        origin.text = destination.text
        destination.text = temp
    }

    /**
     * Diff util - updates the recyclerview asynchronously
     */
    private fun updateList(newList: MutableList<Favorite>) {
        val diffResult = DiffUtil.calculateDiff(FavoriteDiffCallback(this.mFavoriteList, newList))
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ListRowFavorites2Binding) : RecyclerView.ViewHolder(binding.root)
}
