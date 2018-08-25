package com.zuk0.gaijinsmash.riderz.ui.adapter.favorite;

import android.app.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.ui.fragment.bart_results.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.ui.fragment.favorite.FavoritesFragment;
import com.zuk0.gaijinsmash.riderz.data.local.entity.Favorite;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.FullTrip;
import com.zuk0.gaijinsmash.riderz.ui.adapter.trip.TripXMLParser;
import com.zuk0.gaijinsmash.riderz.utils.BartApiUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FavoriteViewAdapter extends ArrayAdapter<Favorite> implements View.OnClickListener {

    private Context mContext; // todo : potential memory leak?
    private FavoritesFragment mFragment;

    public FavoriteViewAdapter(List<Favorite> data, Context context, FavoritesFragment fragment) {
        super(context, R.layout.list_row_bart_favorites, data);
        this.mContext = context;
        this.mFragment = fragment;
    }

    private static class ViewHolder {
        TextView origin;
        TextView destination;
        ImageButton searchButton;
        ImageButton optionsButton;
    }

    @Override
    public void onClick(View v) {
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Favorite favorite = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_row_bart_favorites, parent, false);
            viewHolder.origin = convertView.findViewById(R.id.favorite_origin_textView);
            viewHolder.destination = convertView.findViewById(R.id.favorite_destination_textView);
            viewHolder.searchButton = convertView.findViewById(R.id.favorite_search_ib);
            viewHolder.optionsButton = convertView.findViewById(R.id.favorite_options_ib);

            // onClick => make an API request for Trip results and change to BartResultsFragment
            viewHolder.searchButton.setOnClickListener(v -> {
                String origin = viewHolder.origin.getText().toString();
                String destination = viewHolder.destination.getText().toString();
                new GetTripTask(mFragment, origin, destination, "Today", "Now").execute();
            });

            // onClick => open options menu with inflater
            viewHolder.optionsButton.setOnClickListener(v -> {
                PopupMenu popupMenu = initPopupMenu(mContext, viewHolder.optionsButton, favorite);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> selectFavoriteAction(popupMenu, item.getItemId(), favorite));
            });

            view = convertView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        if (favorite != null) {
            String origin = favorite.getOrigin();
            String destination = favorite.getDestination();
            viewHolder.origin.setText(origin);
            viewHolder.destination.setText(destination);
        }
        return view;
    }

    private void refreshFragment() {
        FragmentTransaction ft = mFragment.getFragmentManager().beginTransaction();
        ft.detach(mFragment).attach(mFragment).commit();
    }

    //todo: allow only two possible favorite objects to have priority?
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

    private boolean selectFavoriteAction(PopupMenu menu, int itemId, Favorite favorite) {
        switch(itemId) {
            case R.id.action_delete_favorite:
                new FavoriteAsyncTask(mFragment, favorite, FavoriteAction.DeleteFavorite).execute();
                menu.dismiss();
                return true;
            case R.id.action_favorite_setPriority:
                new FavoriteAsyncTask(mFragment, favorite, FavoriteAction.SetAsPriority).execute();
                menu.dismiss();
                refreshFragment();
                return true;
            case R.id.action_favorite_deletePriority:
                new FavoriteAsyncTask(mFragment, favorite, FavoriteAction.DeletePriority).execute();
                menu.dismiss();
                refreshFragment();
                return true;
        }
        return false;
    }
    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------

    // todo: abstract this?
    private static class GetTripTask extends AsyncTask<Void, Void, Boolean> {
        private TripXMLParser routeXMLParser = null;
        private List<FullTrip> mTripList = null;
        private WeakReference<FavoritesFragment> mWeakRef;
        private String mDepartAbbr, mArriveAbbr, mDate, mTime;

        private GetTripTask(FavoritesFragment context, String departingStn, String arrivingStn, String date, String time) {
            mWeakRef = new WeakReference<>(context);
            mDate = date;
            mTime = time;
            mDepartAbbr = departingStn;
            mArriveAbbr = arrivingStn;
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            FavoritesFragment frag = mWeakRef.get();
            // Create the API Call
            String uri = BartApiUtils.getDetailedRoute(mDepartAbbr, mArriveAbbr, mDate, mTime);
            try {
                if (frag != null)
                    routeXMLParser = new TripXMLParser(frag.getActivity());
                mTripList = routeXMLParser.getList(uri);
            } catch (IOException | XmlPullParserException e){
                e.printStackTrace();
                if (frag != null)
                    Toast.makeText(frag.getActivity(), frag.getResources().getText(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
            return mTripList != null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            FavoritesFragment frag = mWeakRef.get();
            if (result) {
                // add list to parcelable bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("FullTripList", (ArrayList<? extends Parcelable>) mTripList);
                bundle.putString("Origin", mDepartAbbr);
                bundle.putString("Destination", mArriveAbbr);
                // Switch to BartResultsFragment
                Fragment newFrag = new BartResultsFragment();
                newFrag.setArguments(bundle);
                FragmentManager manager = null;
                if (frag != null)
                    manager =
                    manager = (frag.getActivity()).getSupportFragmentManager(); //todo: check this for null
                if (manager != null)
                    manager.beginTransaction()
                            .replace(R.id.fragmentContent, newFrag)
                            .addToBackStack(null)
                            .commit();
            } else {
                if (frag != null)
                    Toast.makeText(frag.getActivity(), frag.getResources().getString(R.string.error_try_again), Toast.LENGTH_LONG).show();
            }
        }
    }

    private enum FavoriteAction { DeleteFavorite, SetAsPriority, DeletePriority }

    private static class FavoriteAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Favorite mFavorite;
        private FavoriteAction mAction;
        private FavoriteDatabase mDatabase;
        private FavoriteViewAdapter mAdapter;
        private WeakReference<FavoritesFragment> mWeakRef;

        FavoriteAsyncTask(FavoritesFragment context, Favorite favorite, FavoriteAction action) {
            this.mWeakRef = new WeakReference<>(context);
            this.mFavorite = favorite;
            this.mAction = action;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            FavoritesFragment frag = mWeakRef.get();
            if(mDatabase == null && frag != null)
                mDatabase = FavoriteDatabase.getRoomDB(frag.getActivity());
            switch(mAction) {
                case DeleteFavorite:
                    if (mDatabase != null) {
                        mDatabase.getFavoriteDAO().delete(mFavorite);
                        return mDatabase.getFavoriteDAO().getFavorite(mFavorite.getOrigin(), mFavorite.getDestination()) == null;
                    }
                    break;
                case SetAsPriority:
                    if(mDatabase != null) {
                        // todo: check count of priorities
                        List<Favorite> favList = null;//mDatabase.getFavoriteDAO().(Favorite.Priority.ON);
                        if(favList.size() > 2){
                            Toast.makeText(frag.getActivity(), "Up to two Favorites may be prioritized.", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        mDatabase.getFavoriteDAO().setPriorityById(mFavorite.getId(), Favorite.Priority.ON);
                        return mDatabase.getFavoriteDAO().getPriorityById(mFavorite.getId()) != 0;
                    }
                    break;
                case DeletePriority:
                    if(mDatabase != null) {
                        mDatabase.getFavoriteDAO().removePriorityById(mFavorite.getId());
                        return mDatabase.getFavoriteDAO().getPriorityById(mFavorite.getId()) == 0;
                    }
                    break;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            FavoritesFragment frag = mWeakRef.get();
            ListView listView;
            if (frag != null) {
                listView = frag.getActivity().findViewById(R.id.bartFavorites_listView);
                if(result) {
                    mAdapter = (FavoriteViewAdapter) listView.getAdapter();

                    switch(mAction) {
                        case DeleteFavorite:
                            mAdapter.remove(mFavorite);
                            listView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            if(listView.getCount() == 0) {
                                TextView tv = ((Activity) frag.getActivity()).findViewById(R.id.bartFavorites_error_tV);
                                tv.setText(frag.getActivity().getResources().getString(R.string.bart_favorites_empty));
                            }
                            Toast.makeText(frag.getActivity(), R.string.deletion_success, Toast.LENGTH_SHORT).show();
                            break;
                        case SetAsPriority:
                            Toast.makeText(frag.getActivity(), R.string.priority_set, Toast.LENGTH_SHORT).show();
                            break;
                        case DeletePriority:
                            Toast.makeText(frag.getActivity(), R.string.priority_deleted, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        }
    }
}

