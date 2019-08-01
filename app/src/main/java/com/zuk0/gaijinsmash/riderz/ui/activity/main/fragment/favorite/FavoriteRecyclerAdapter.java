package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.favorite;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.diff_util.FavoriteDiffCallback;
import com.zuk0.gaijinsmash.riderz.databinding.ListRowFavorites2Binding;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment;
import com.zuk0.gaijinsmash.riderz.ui.shared.adapter.RecyclerAdapterClickListener;

import java.util.List;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> implements RecyclerAdapterClickListener {

    private List<Favorite> mFavoriteList;
    private View.OnClickListener listener; //todo  - use this as general listener for all buttons
    private Fragment mParent; //todo replace with rxjava?

    public FavoriteRecyclerAdapter(List<Favorite> favoriteList, Fragment parent) {
        mFavoriteList = favoriteList;
        mParent = parent; //todo memory leak
    }

    @Override
    public void setClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListRowFavorites2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_row_favorites2, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = mFavoriteList.get(position);
        String stationA = StationList.getAbbrFromStationName(favorite.getA().getName());
        String stationB = StationList.getAbbrFromStationName(favorite.getB().getName());
        holder.binding.departStation.setText(stationA);
        holder.binding.arriveStation.setText(stationB);
        holder.binding.settingsButton.setOnClickListener(v -> initPopupMenu(v.getContext(), holder.binding.settingsButton, favorite, position));
        if(favorite.getPriority() == Favorite.Priority.ON) {
            holder.binding.background.setBackgroundColor(holder.binding.getRoot().getContext().getResources().getColor(R.color.primaryLightColor));
        }
        holder.binding.switchButton.setOnClickListener(v -> reverseRoute(holder.binding.departStation, holder.binding.arriveStation));
        holder.binding.getTripButton.setOnClickListener(v -> initTripSearch(mParent, holder.binding.departStation, holder.binding.arriveStation));
    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }

    private void initPopupMenu(Context context, ImageButton ib, Favorite favorite, int rowPosition) {
        PopupMenu popupMenu = new PopupMenu(context, ib);
        popupMenu.getMenuInflater().inflate(R.menu.favorite_options, popupMenu.getMenu());
        popupMenu.show();
        if (favorite != null) {
            if(favorite.getPriority() == Favorite.Priority.ON)  {
                Log.i("PRIORITY", "ON");
                MenuItem deletePriorityItem = popupMenu.getMenu().findItem(R.id.action_favorite_deletePriority);
                deletePriorityItem.setVisible(true);
            } else {
                MenuItem setPriorityItem = popupMenu.getMenu().findItem(R.id.action_favorite_setPriority);
                setPriorityItem.setVisible(true);
            }
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            selectFavoriteAction(popupMenu, context, item.getItemId(), favorite, rowPosition);
            return true;
        });
    }

    private void selectFavoriteAction(PopupMenu menu, Context context, int itemId, Favorite favorite, int rowPosition) {
        switch(itemId) {
            case R.id.action_delete_favorite:
                FavoritesViewModel.deleteFavorite(context, favorite);
                menu.dismiss();
                Toast.makeText(context, R.string.deletion_success, Toast.LENGTH_SHORT).show();
                this.notifyItemRemoved(rowPosition);
                if(getItemCount() == 1) {
                    mFavoriteList.clear();
                    notifyDataSetChanged();
                }
                break;
            case R.id.action_favorite_setPriority:
                //todo: prompt user if he/she wants to remove current priority, and make this the priority
                FavoritesViewModel.addPriority(context, favorite);
                menu.dismiss();
                notifyItemChanged(rowPosition);
                break;
            case R.id.action_favorite_deletePriority:
                FavoritesViewModel.removePriority(context, favorite);
                menu.dismiss();
                Toast.makeText(context, R.string.priority_deleted, Toast.LENGTH_SHORT).show();
                notifyItemRemoved(rowPosition);
                break;
        }
    }

    /*
        Note: pulls text directly from textviews, which are in Abbreviated format
        Must get full stations names for sql query in favorites database.
    */
    private void initTripSearch(Fragment parent, TextView origin, TextView destination) {
        String originString = origin.getText().toString();
        String destinationString = destination.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString(TripFragment.TripBundle.ORIGIN.getValue(), originString);
        bundle.putString(TripFragment.TripBundle.DESTINATION.getValue(), destinationString);
        bundle.putString(TripFragment.TripBundle.DATE.getValue(), "TODAY");
        bundle.putString(TripFragment.TripBundle.TIME.getValue(), "NOW");
        bundle.putBoolean("FAVORITE_RECYCLER_ADAPTER", true);

        NavHostFragment.findNavController(parent).navigate(R.id.action_favoritesFragment_to_resultsFragment, bundle, null, null);
    }

    private void reverseRoute(TextView origin, TextView destination) {
        String temp = origin.getText().toString();
        origin.setText(destination.getText());
        destination.setText(temp);
    }

    /**
     * Diff util - updates the recyclerview asynchronously
     */
    private void updateList(List<Favorite> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FavoriteDiffCallback(this.mFavoriteList, newList));
        diffResult.dispatchUpdatesTo(this);
    }

    private List<Favorite> getFavoritesFromDb() {
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ListRowFavorites2Binding binding;

        ViewHolder(ListRowFavorites2Binding binding) {
            super(binding.getRoot());
            this.binding  = binding;
        }
    }
}
