package com.example.gaijinsmash.transitapp.activity.fragment;



import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;


public class HomeFragment extends Fragment {

    private Button findNearestBtn;
    private Button mapBtn;
    private Button routeBtn;
    private TextView textView = null;
    //private ItemFragment.OnListFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View mInflatedView = inflater.inflate(R.layout.home_view, container, false);

        findNearestBtn = (Button) mInflatedView.findViewById(R.id.home_view_btn1);
        findNearestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "example", Toast.LENGTH_SHORT).show();
                //new GetGPSTask(getContext()).execute();
            }
        });

        mapBtn   = (Button) mInflatedView.findViewById(R.id.home_view_btn2);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new MapFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home_container, frag, "map frag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        routeBtn = (Button) mInflatedView.findViewById(R.id.home_view_btn3);
        routeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new ScheduleFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_home_container, frag, "schedule frag")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return mInflatedView;
    }

    // TODO: Warn user if there's no internet connection

    // TODO: Display up-to-date news on BART
    // TODO: Display weather in local area - requires location
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemFragment.OnListFragmentInteractionListener) {
            mListener = (ItemFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */

    private class GetGPSTask extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;

        public GetGPSTask(Context mContext) {
            if(this.mContext == null) {
                this.mContext = mContext;
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // TODO: insert GPS logic here
            return null;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                // Do something
            } else {
                // Do something
            }
        }
    }
}
