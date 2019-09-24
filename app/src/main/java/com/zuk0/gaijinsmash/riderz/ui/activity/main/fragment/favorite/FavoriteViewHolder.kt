package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite
import com.zuk0.gaijinsmash.riderz.data.local.entity.event.EventLaunchFragment
import com.zuk0.gaijinsmash.riderz.data.local.room.dao.FavoriteDao
import com.zuk0.gaijinsmash.riderz.data.local.room.database.FavoriteDatabase
import com.zuk0.gaijinsmash.riderz.databinding.ListRowFavorites2Binding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment
import org.greenrobot.eventbus.EventBus

/**
 * ViewHolders already provide functionality to RecyclerView Adapter properties, like item position and viewType
 */
class FavoriteViewHolder(val binding: ListRowFavorites2Binding, val adapter: FavoriteRecyclerAdapter) : RecyclerView.ViewHolder(binding.root) {

    init {
        val favorite = adapter.mFavoriteList.get(adapterPosition)
        binding.settingsButton.setOnClickListener { v -> displayPopupMenu(v.context, binding.settingsButton, favorite, adapterPosition) }
        if (favorite.priority === Favorite.Priority.ON) {
            binding.background.setBackgroundColor(binding.root.context.resources.getColor(R.color.primaryLightColor))
        }
        binding.switchButton.setOnClickListener { v -> reverseRoute(binding.departStation, binding.arriveStation) }
        binding.getTripButton.setOnClickListener { v -> initTripSearch(binding.departStation, binding.arriveStation) }
    }

    /*

     */

    private fun displayPopupMenu(context: Context, ib: ImageButton, favorite: Favorite?, rowPosition: Int) {
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
        if(favorite == null) {
            //todo
            return
        }
        when (itemId) {
            R.id.action_delete_favorite -> {
                FavoritesViewModel.deleteFavorite(context, favorite)
                menu.dismiss()
                Toast.makeText(context, R.string.deletion_success, Toast.LENGTH_SHORT).show()
                adapter.updateList(getNewList())
            }
            R.id.action_favorite_setPriority -> {
                //todo: prompt user if he/she wants to remove current priority, and make this the priority
                FavoritesViewModel.addPriority(context, favorite)
                menu.dismiss()
                adapter.updateList(getNewList())
            }
            R.id.action_favorite_deletePriority -> {
                FavoritesViewModel.removePriority(context, favorite)
                menu.dismiss()
                Toast.makeText(context, R.string.priority_deleted, Toast.LENGTH_SHORT).show()
                adapter.updateList(getNewList())
            }
        }
    }

    /*
        Note: pulls text directly from textviews, which are in Abbreviated format
        Must get full stations names for sql query in favorites database.
    */
    private fun initTripSearch(origin: TextView, destination: TextView) {
        val originString = origin.text.toString()
        val destinationString = destination.text.toString()

        val bundle = Bundle()
        bundle.putString(TripFragment.TripBundle.ORIGIN.value, originString)
        bundle.putString(TripFragment.TripBundle.DESTINATION.value, destinationString)
        bundle.putString(TripFragment.TripBundle.DATE.value, "TODAY")
        bundle.putString(TripFragment.TripBundle.TIME.value, "NOW")
        bundle.putBoolean("FAVORITE_RECYCLER_ADAPTER", true)

        Navigation.findNavController(binding.root).navigate(R.id.action_favoritesFragment_to_resultsFragment, bundle, null, null)
    }

    private fun reverseRoute(origin: TextView, destination: TextView) {
        val temp = origin.text.toString()
        origin.text = destination.text
        destination.text = temp
    }

    private fun getNewList() : MutableList<Favorite> {
        val db = FavoriteDatabase.getRoomDB(binding.root.context)
        val list = db.favoriteDAO.list
        db.close()
        return list
    }
}