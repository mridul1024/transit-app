package com.zuk0.gaijinsmash.riderz.ui.adapter.favorite;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.ui.fragment.favorite.FavoritesViewModel;

import java.util.List;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView origin;
        TextView destination;
        ImageButton searchButton;
        ImageButton optionsButton;

        ViewHolder(View view) {
            super(view);
            origin = view.findViewById(R.id.favorite_origin_textView);
            destination = view.findViewById(R.id.favorite_destination_textView);
            searchButton = view.findViewById(R.id.favorite_search_ib);
            optionsButton = view.findViewById(R.id.favorite_options_ib);
        }
    }

    private List<Favorite> mFavoriteList;

    public FavoriteRecyclerAdapter(List<Favorite> favoriteList) { mFavoriteList = favoriteList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_bart_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = mFavoriteList.get(position);
        holder.origin.setText(favorite.getOrigin());
        holder.destination.setText(favorite.getDestination());

        // onClick => make an API request for Trip results and change to BartResultsFragment
        holder.searchButton.setOnClickListener(v -> {
            String origin = holder.origin.getText().toString();
            String destination = holder.destination.getText().toString();
            //todo : getTrip from ViewModel
            //new FavoriteViewAdapter.GetTripTask(mFragment, origin, destination, "Today", "Now").execute();
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
}
