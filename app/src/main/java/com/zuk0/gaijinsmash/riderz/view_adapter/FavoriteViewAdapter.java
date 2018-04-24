package com.zuk0.gaijinsmash.riderz.view_adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import com.zuk0.gaijinsmash.riderz.database.FavoriteDatabase;
import com.zuk0.gaijinsmash.riderz.fragment.BartResultsFragment;
import com.zuk0.gaijinsmash.riderz.fragment.FavoritesFragment;
import com.zuk0.gaijinsmash.riderz.model.bart.Favorite;
import com.zuk0.gaijinsmash.riderz.model.bart.FullTrip;
import com.zuk0.gaijinsmash.riderz.network.xmlparser.TripXMLParser;
import com.zuk0.gaijinsmash.riderz.utils.BartApiStringBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FavoriteViewAdapter extends ArrayAdapter<Favorite> implements View.OnClickListener {

    private Context mContext;
    private FavoritesFragment mFragment;

    public FavoriteViewAdapter(List<Favorite> data, Context context, FavoritesFragment fragment) {
        super(context, R.layout.bart_favorites_list_row, data);
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
            convertView = inflater.inflate(R.layout.bart_favorites_list_row, parent, false);
            viewHolder.origin = convertView.findViewById(R.id.favorite_origin_textView);
            viewHolder.destination = convertView.findViewById(R.id.favorite_destination_textView);
            viewHolder.searchButton = convertView.findViewById(R.id.favorite_search_ib);

            // onClick => make an API request for Trip results and change to BartResultsFragment
            viewHolder.searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView originTV = viewHolder.origin;
                    String origin = originTV.getText().toString();
                    TextView destinationTV = viewHolder.destination;
                    String destination = destinationTV.getText().toString();

                    //Time and Date
                    new GetTripTask(mFragment, origin, destination, "Today", "Now").execute();
                }
            });

            // onClick => open options menu with inflater
            viewHolder.optionsButton = convertView.findViewById(R.id.favorite_options_ib);
            viewHolder.optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, viewHolder.optionsButton);
                    popup.getMenuInflater().inflate(R.menu.favorite_options, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()) {
                                case R.id.action_delete_favorite:
                                    new FavoriteAsyncTask(mFragment, favorite, 0).execute();
                                    return true;
                                case 1:
                                    //todo: call to database and update color
                                    break;
                                case 2:
                                    //todo: add change priority in listview (sorting)
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });

            view = convertView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        String origin = null;
        if (favorite != null) {
            origin = favorite.getOrigin();
        }
        String destination = null;
        if (favorite != null) {
            destination = favorite.getDestination();
        }
        viewHolder.origin.setText(origin);
        viewHolder.destination.setText(destination);
        return view;
    }

    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------

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
            String uri = BartApiStringBuilder.getDetailedRoute(mDepartAbbr, mArriveAbbr, mDate, mTime);
            try {
                routeXMLParser = new TripXMLParser(frag.getActivity());
                mTripList = routeXMLParser.getList(uri);
            } catch (IOException | XmlPullParserException e){
                e.printStackTrace();
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
                FragmentManager manager = (frag.getActivity()).getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.fragmentContent, newFrag)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(frag.getActivity(), frag.getResources().getString(R.string.error_try_again), Toast.LENGTH_LONG).show();
            }
        }
    }

    private static class FavoriteAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Favorite mFavorite;
        private int mStatus, mToastNumber;
        private FavoriteDatabase mDatabase;
        private FavoriteViewAdapter adapter;
        private WeakReference<FavoritesFragment> mWeakRef;

        FavoriteAsyncTask(FavoritesFragment context, Favorite favorite, int status) {
            this.mWeakRef = new WeakReference<>(context);
            this.mFavorite = favorite;
            this.mStatus = status;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            FavoritesFragment frag = mWeakRef.get();
            if(mDatabase == null) {
                mDatabase = FavoriteDatabase.getRoomDB(frag.getActivity());
            }
            switch(mStatus) {
                case 0:
                    //todo: check for successful deletion before
                    mDatabase.getFavoriteDAO().delete(mFavorite);
                    mToastNumber = 0;

                    //check if deletion was successful
                    return mDatabase.getFavoriteDAO().getFavorite(mFavorite.getOrigin(), mFavorite.getDestination()) == null;
                case 1:
                    //todo: add change color
                case 2:
                    //todo: add priority
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            FavoritesFragment frag = mWeakRef.get();
            ListView listView = frag.getActivity().findViewById(R.id.bartFavorites_listView);
            if(result) {
                switch(mToastNumber) {
                    case 0:
                        adapter = (FavoriteViewAdapter) listView.getAdapter();

                        // delete object from adapter
                        adapter.remove(mFavorite);

                        int count = listView.getCount();
                        listView.setAdapter(adapter);

                        if(count == 0) {
                            TextView tv = ((Activity) frag.getActivity()).findViewById(R.id.bartFavorites_error_tV);
                            tv.setText(frag.getActivity().getResources().getString(R.string.bart_favorites_empty));
                        }
                        Toast.makeText(frag.getActivity(), R.string.deletion_success, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(frag.getActivity(), R.string.error_try_again, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
