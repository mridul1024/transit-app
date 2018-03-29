package com.example.gaijinsmash.transitapp.view_adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
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

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.database.FavoriteDatabase;
import com.example.gaijinsmash.transitapp.fragment.BartResultsFragment;
import com.example.gaijinsmash.transitapp.model.bart.Favorite;
import com.example.gaijinsmash.transitapp.model.bart.FullTrip;
import com.example.gaijinsmash.transitapp.network.xmlparser.TripXMLParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteViewAdapter extends ArrayAdapter<Favorite> implements View.OnClickListener {

    private Context mContext;
    private List<Favorite> mFavoriteList;
    private ListView mListView;

    public FavoriteViewAdapter(List<Favorite> data, Context context, ListView listView) {
        super(context, R.layout.favorite_list_row, data);
        this.mContext = context;
        this.mFavoriteList = data;
        this.mListView = listView;
    }

    private static class ViewHolder {
        TextView origin;
        TextView destination;
        ImageButton searchButton;
        ImageButton optionsButton;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Favorite favorite = (Favorite) object;
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
            convertView = inflater.inflate(R.layout.favorite_list_row, parent, false);
            viewHolder.origin = (TextView) convertView.findViewById(R.id.favorite_origin_textView);
            viewHolder.destination = (TextView) convertView.findViewById(R.id.favorite_destination_textView);
            viewHolder.searchButton = (ImageButton) convertView.findViewById(R.id.favorite_search_ib);

            // onClick => make an API request for Trip results and change to BartResultsFragment
            viewHolder.searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView originTV = (TextView) viewHolder.origin;
                    String origin = originTV.getText().toString();
                    TextView destinationTV = (TextView) viewHolder.destination;
                    String destination = destinationTV.getText().toString();

                    //Time and Date
                    new GetTripTask(mContext, origin, destination, "Today", "Now").execute();
                }
            });

            // onClick => open options menu with inflater
            viewHolder.optionsButton = (ImageButton) convertView.findViewById(R.id.favorite_options_ib);
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
                                    new FavoriteAsyncTask(favorite, 0).execute();
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
            convertView.setTag(viewHolder);
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
        return convertView;
    }

    //todo: abstract this async class - slightly redundant in TripFragment
    //---------------------------------------------------------------------------------------------
    // AsyncTask
    //---------------------------------------------------------------------------------------------

    private class GetTripTask extends AsyncTask<Void, Void, Boolean> {
        private TripXMLParser routeXMLParser = null;
        private List<FullTrip> mTripList = null;
        private Context mContext;
        private String mDepartAbbr, mArriveAbbr, mDate, mTime;

        private GetTripTask(Context context, String departingStn, String arrivingStn, String date, String time) {
            if(this.mContext == null) {
                mContext = context;
                mDate = date;
                mTime = time;
                mDepartAbbr = departingStn;
                mArriveAbbr = arrivingStn;
            }
        }

        @Override
        protected Boolean doInBackground(Void...voids) {
            boolean result = false;
            // Create the API Call
            String uri = ApiStringBuilder.getDetailedRoute(mDepartAbbr, mArriveAbbr, mDate, mTime);
            try {
                routeXMLParser = new TripXMLParser(mContext);
                mTripList = routeXMLParser.getList(uri);
            } catch (IOException | XmlPullParserException e){
                e.printStackTrace();
                Toast.makeText(mContext, mContext.getResources().getText(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
            if (mTripList != null) {
                result = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // add list to parcelable bundle
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("TripList", (ArrayList<? extends Parcelable>) mTripList);
                bundle.putString("Origin", mDepartAbbr);
                bundle.putString("Destination", mArriveAbbr);
                // Switch to BartResultsFragment
                Fragment newFrag = new BartResultsFragment();
                newFrag.setArguments(bundle);
                FragmentManager manager = ((Activity) mContext).getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.fragmentContent, newFrag)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.error_try_again), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class FavoriteAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private Favorite mFavorite;
        private int mStatus, mColor, mToastNumber;
        private FavoriteDatabase mDatabase;
        FavoriteViewAdapter adapter;

        private ViewGroup mParent;

        public FavoriteAsyncTask(Favorite favorite, int status) {
            this.mFavorite = favorite;
            this.mStatus = status;
            if(mDatabase == null) {
                mDatabase = FavoriteDatabase.getRoomDB(mContext);
            }
        }

        //todo: need to complete color option on menu
        public FavoriteAsyncTask(Favorite favorite, int color, int status) {
            this.mFavorite = favorite;
            this.mColor = color;

            this.mStatus = status;
            if(mDatabase == null) {
                mDatabase = FavoriteDatabase.getRoomDB(mContext);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            switch(mStatus) {
                case 0:
                    Log.i("delete", "was selected");
                    //todo: check for successful deletion before
                    mDatabase.getFavoriteDAO().delete(mFavorite);
                    mToastNumber = 0;

                    //check if deletion was successful
                    if(mDatabase.getFavoriteDAO().getFavorite(mFavorite.getOrigin(), mFavorite.getDestination()) == null) {
                        return true;
                    }
                    return false;
                case 1:
                    //todo: add change color
                case 2:
                    //todo: add priority
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                switch(mToastNumber) {
                    case 0:
                        adapter = (FavoriteViewAdapter) mListView.getAdapter();

                        // delete object from adapter
                        adapter.remove(mFavorite);

                        int count = 0;
                        count = mListView.getCount();
                        Log.i("LIST VIEW COUNT", String.valueOf(count));
                        int favCount = 0;
                        favCount = mFavoriteList.size();
                        Log.i("fav list", String.valueOf(favCount));

                        Log.i("fav", mFavorite.getOrigin());

                        // todo: this strangely does not work properly, and unable to remove the last item in row
                        //adapter.notifyDataSetChanged();

                        // need to refresh adapter
                        mListView.setAdapter(adapter);

                        if(count == 0) {
                            TextView tv = ((Activity) mContext).findViewById(R.id.bartFavorites_error_tV);
                            tv.setText(mContext.getResources().getString(R.string.bart_favorites_empty));
                        }
                        Toast.makeText(mContext, R.string.deletion_success, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, R.string.error_try_again, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
