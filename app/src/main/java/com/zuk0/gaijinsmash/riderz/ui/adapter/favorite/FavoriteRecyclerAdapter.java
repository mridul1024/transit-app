package com.zuk0.gaijinsmash.riderz.ui.adapter.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.StationList;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.ui.fragment.favorite.FavoritesViewModel;
import com.zuk0.gaijinsmash.riderz.ui.fragment.trip.TripFragment;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.favorite_origin_textView) TextView origin;
        @BindView(R.id.favorite_destination_textView) TextView destination;
        @BindView(R.id.favorite_search_ib) ImageButton searchButton;
        @BindView(R.id.favorite_options_ib) ImageButton optionsButton;
        @BindView(R.id.favorite_reverse_ib) ImageButton reverseButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private List<Favorite> mFavoriteList;

    public FavoriteRecyclerAdapter(List<Favorite> favoriteList) { mFavoriteList = favoriteList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = mFavoriteList.get(position);

        String originAbbr = getAbbrFromStationName(favorite.getDestinationTrip().getOrigin());
        String destAbbr = getAbbrFromStationName(favorite.getDestinationTrip().getDestination());
        holder.origin.setText(originAbbr);
        holder.destination.setText(destAbbr);
        holder.reverseButton.setOnClickListener(v -> {
            String temp = holder.origin.getText().toString();
            holder.origin.setText(holder.destination.getText());
            holder.destination.setText(temp);
        });

        // onClick => make an API request for Trip results and change to BartResultsFragment
        holder.searchButton.setOnClickListener(v -> {
            String origin = holder.origin.getText().toString();
            String destination = holder.destination.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString(TripFragment.TripBundle.ORIGIN.getValue(), origin);
            bundle.putString(TripFragment.TripBundle.DESTINATION.getValue(), destination);
            bundle.putString(TripFragment.TripBundle.DATE.getValue(), "TODAY");
            bundle.putString(TripFragment.TripBundle.TIME.getValue(), "NOW");
            Fragment newFrag = new BartResultsFragment();
            newFrag.setArguments(bundle);
            FragmentManager manager = ((MainActivity) v.getContext()).getSupportFragmentManager();
            FragmentTransaction transaction;
            if(manager != null) {
                transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContent, newFrag).addToBackStack(null).commit();
            }

        });

        // onClick => open options menu with inflater
        holder.optionsButton.setOnClickListener(v -> {
            PopupMenu popupMenu = initPopupMenu(v.getContext(), holder.optionsButton, favorite);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> selectFavoriteAction(popupMenu, v.getContext(), item.getItemId(), favorite));
        });
    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }

    private PopupMenu initPopupMenu(Context context, ImageButton button, Favorite favorite){
        PopupMenu popupMenu = new PopupMenu(context, button);
        popupMenu.getMenuInflater().inflate(R.menu.favorite_options, popupMenu.getMenu());
        if (favorite != null) {
            if(favorite.getPriority() == Favorite.Priority.ON)  {
                MenuItem deletePriorityItem = popupMenu.getMenu().findItem(R.id.action_favorite_deletePriority);
                deletePriorityItem.setVisible(true);
            } else {
                MenuItem setPriorityItem = popupMenu.getMenu().findItem(R.id.action_favorite_setPriority);
                setPriorityItem.setVisible(true);
            }
        }
        return popupMenu;
    }

    private boolean selectFavoriteAction(PopupMenu menu, Context context, int itemId, Favorite favorite) {
        switch(itemId) {
            case R.id.action_delete_favorite:
                FavoritesViewModel.deleteFavorite(context, favorite);
                menu.dismiss();
                if(FavoritesViewModel.getFavoriteCount(context) == 0) {
                    //todo : update view
                    // tv.setText(frag.getActivity().getResources().getString(R.string.bart_favorites_empty));
                    return false;
                }
                Toast.makeText(context, R.string.deletion_success, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_favorite_setPriority:
                if(FavoritesViewModel.doesPriorityExist(context)) {
                    //todo: if current == priority
                    Toast.makeText(context, "Only one Favorite can be priority.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    FavoritesViewModel.setPriority(context, favorite.getId());
                    menu.dismiss();
                    Toast.makeText(context, R.string.priority_set, Toast.LENGTH_SHORT).show();
                    return true;
                }
                //refreshFragment();
            case R.id.action_favorite_deletePriority:
                FavoritesViewModel.removePriority(context, favorite.getId());
                menu.dismiss();
                Toast.makeText(context, R.string.priority_deleted, Toast.LENGTH_SHORT).show();
                //refreshFragment();
        }
        return false;
    }

    private String getAbbrFromStationName(String name) {
        String abbr = "";
        for(Map.Entry<String, String> e : StationList.stationMap.entrySet()) {
            if(e.getValue().equalsIgnoreCase(name)) {
                abbr = e.getKey();
            }
        }
        return abbr;
    }


}
