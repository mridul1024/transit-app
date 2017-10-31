package com.example.gaijinsmash.transitapp.activity.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.adapter.AdvisoryCustomAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Advisory;
import com.example.gaijinsmash.transitapp.network.CheckInternet;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.AdvisoryXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button findNearestBtn;
    private Button mapBtn;
    private Button routeBtn;

    private TextView bsaDateTv = null;
    private TextView bsaTimeTv = null;
    private ListView bsaListView;
    //private ItemFragment.OnListFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View mInflatedView = inflater.inflate(R.layout.home_view, container, false);

        // TODO: Warn user if there's no internet connection

        // TODO: Display up-to-date news on BART - Change this to a background service.
        new GetAdvisoryTask(getActivity()).execute();

        bsaDateTv = (TextView) mInflatedView.findViewById(R.id.home_view_dateTv);
        bsaTimeTv = (TextView) mInflatedView.findViewById(R.id.home_view_timeTv);
        bsaListView = mInflatedView.findViewById(R.id.advisory_listView);

        findNearestBtn = (Button) mInflatedView.findViewById(R.id.home_view_btn1);
        findNearestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "example", Toast.LENGTH_SHORT).show();
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

    private class GetAdvisoryTask extends AsyncTask<Void, Void, List<Advisory>> {
        private Context mContext;
        private List<Advisory> mList;

        public GetAdvisoryTask(Context mContext) {
            if(this.mContext == null)
                this.mContext = mContext;
            mList = new ArrayList<Advisory>();
        }

        @Override
        protected List<Advisory> doInBackground(Void... voids) {
            try {
                ApiStringBuilder builder = new ApiStringBuilder();
                FetchInputStream is = new FetchInputStream(mContext);
                InputStream in = is.connectToApi(builder.getBSA());
                AdvisoryXmlParser parser = new AdvisoryXmlParser(mContext);
                mList = parser.parse(in);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return mList;
        }

        protected void onPostExecute(List<Advisory> list) {
            // find time and date separately

            for(Advisory adv : list) {
                if(adv.getDate() != null)
                    bsaDateTv.setText(adv.getDate());
                if(adv.getTime() != null)
                    bsaTimeTv.setText(adv.getTime());
            }
            AdvisoryCustomAdapter adapter = new AdvisoryCustomAdapter(list, mContext);
            bsaListView.setAdapter(adapter);
        }
    }
}
