package com.example.gaijinsmash.transitapp.activity.fragment;


// TODO: is android.Fragment different?

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
import com.example.gaijinsmash.transitapp.model.bart.Station;
import com.example.gaijinsmash.transitapp.xmlparser.StationXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


/**
 * Created by ryanj on 6/30/2017.
 */


public class HomeFragment extends Fragment {

    private TextView textView = null;
    //private ItemFragment.OnListFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View mInflatedView = inflater.inflate(R.layout.home_view, container, false);

        Button findNearestBtn = (Button) mInflatedView.findViewById(R.id.home_view_btn);
        findNearestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get gps location and find nearest station
            }
        });

        // TEST ------------------------------------------------------------------------------------
        Button testButton = (Button) mInflatedView.findViewById(R.id.home_view_testBtn);
        textView = (TextView) mInflatedView.findViewById(R.id.home_view_textView);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG);
                toast.show();

                new TestInternetTask().execute();
            }
        });
        // TEST ------------------------------------------------------------------------------------

        return mInflatedView;
    }

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
    // TODO: Warn user if there's no internet connection
    // TODO: Display up-to-date news on BART
    // TODO: Display weather in local area - requires location
    // TODO: Keep our trains clean!
    // TODO: Report suspicious activity and keep your belongings safe

    // TODO: IF Holiday - show button

    private class TestInternetTask extends AsyncTask<Void, Void, Boolean> {

        private StationXMLParser stationXMLParser = new StationXMLParser();
        private List<Station> stationList = null;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                stationList = stationXMLParser.testCall();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            if (stationList != null) {
                return true;
            } else {
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if(result) {

                for(Station station : stationList) {
                    textView.setText(station.getName());
                }

            } else {
                textView.setText("Error");
            }
        }
    }
}