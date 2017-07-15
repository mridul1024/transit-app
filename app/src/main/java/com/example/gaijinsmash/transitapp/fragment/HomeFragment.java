package com.example.gaijinsmash.transitapp.fragment;


// TODO: is android.Fragment different?
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaijinsmash.transitapp.R;
import com.example.gaijinsmash.transitapp.internet.InternetOperations;
import com.example.gaijinsmash.transitapp.xmlparser.StationXMLParser;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


/**
 * Created by ryanj on 6/30/2017.
 */


public class HomeFragment extends Fragment {

    private List results = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View mInflatedView = inflater.inflate(R.layout.home_view, container, false);

        Button testButton = (Button) mInflatedView.findViewById(R.id.home_view_testBtn);
        final TextView textView = (TextView) mInflatedView.findViewById(R.id.home_view_textView);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return mInflatedView;
    }
}
