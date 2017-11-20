package com.example.gaijinsmash.transitapp.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.view_adapter.AdvisoryCustomViewAdapter;
import com.example.gaijinsmash.transitapp.model.bart.Advisory;
import com.example.gaijinsmash.transitapp.network.FetchInputStream;
import com.example.gaijinsmash.transitapp.network.xmlparser.AdvisoryXmlParser;
import com.example.gaijinsmash.transitapp.utils.ApiStringBuilder;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView bmBsaDateTv = null;
    private TextView mBsaTimeTv = null;
    private ListView mBsaListView;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.home_view, container, false);
        mContext = getActivity();

        // TODO: Warn user if there's no internet connection

        // TODO: Display up-to-date news on BART - Change this to a splash_background service.
        new GetAdvisoryTask(mContext).execute();

        mBsaTimeTv = (TextView) inflatedView.findViewById(R.id.home_view_timeTv);
        mBsaListView = inflatedView.findViewById(R.id.advisory_listView);

        return inflatedView;
    }

    //---------------------------------------------------------------------------------------------
    // Lifecycle Events
    //---------------------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
            for(Advisory adv : list) {
                //if(adv.getDate() != null)
                  //  bsaDateTv.setText(adv.getDate());
                if(adv.getTime() != null)
                    mBsaTimeTv.setText(getText(R.string.last_update) + " " + adv.getTime());
            }
            AdvisoryCustomViewAdapter adapter = new AdvisoryCustomViewAdapter(list, mContext);
            mBsaListView.setAdapter(adapter);
        }
    }
}