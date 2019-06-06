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

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.constants.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.trip.TripFragment;
import com.zuk0.gaijinsmash.riderz.ui.shared.adapter.RecyclerAdapterClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> implements RecyclerAdapterClickListener {

    private List<Favorite> mFavoriteList;
    private View.OnClickListener listener;
    private Fragment mParent;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_favorites, parent, false);
        ViewHolder holder = new ViewHolder(view);
        //holder.itemView.setOnClickListener( v -> listener.onClick(v));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = mFavoriteList.get(position);
        String originAbbr = StationList.getAbbrFromStationName(favorite.getOrigin());
        String destAbbr = StationList.getAbbrFromStationName(favorite.getDestination());
        holder.origin.setText(originAbbr.toUpperCase());
        holder.destination.setText(destAbbr.toUpperCase());
        holder.optionsButton.setOnClickListener(v -> initPopupMenu(v.getContext(), holder.optionsButton, favorite, position));
        if(favorite.getPriority() == Favorite.Priority.ON) {
            holder.background.setBackgroundColor(holder.origin.getContext().getResources().getColor(R.color.primaryLightColor));
        }
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
                    this.notifyDataSetChanged();
                }
                break;
            case R.id.action_favorite_setPriority:
                //todo: prompt user if he/she wants to remove current priority, and make this the priority
                FavoritesViewModel.addPriority(context, favorite);
                menu.dismiss();
                this.notifyItemChanged(rowPosition);
                break;
            case R.id.action_favorite_deletePriority:
                FavoritesViewModel.removePriority(context, favorite);
                menu.dismiss();
                Toast.makeText(context, R.string.priority_deleted, Toast.LENGTH_SHORT).show();
                this.notifyItemChanged(rowPosition);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView origin;
        private TextView destination;
        private ImageButton searchButton;
        private ImageButton optionsButton;
        private ImageButton reverseButton;
        private LinearLayout background;

        ViewHolder(View view) {
            super(view);
            origin = view.findViewById(R.id.favorite_origin_textView);
            destination = view.findViewById(R.id.favorite_destination_textView);
            searchButton = view.findViewById(R.id.favorite_search_ib);
            optionsButton = view.findViewById(R.id.favorite_options_ib);
            reverseButton = view.findViewById(R.id.favorite_reverse_ib);
            background = view.findViewById(R.id.favorite_row_background);

            reverseButton.setOnClickListener(v -> reverseRoute(origin, destination));
            searchButton.setOnClickListener(v -> initTripSearch(mParent, origin, destination));
        }
    }
}
