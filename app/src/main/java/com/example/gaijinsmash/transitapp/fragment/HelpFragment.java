package com.example.gaijinsmash.transitapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gaijinsmash.transitapp.R;

public class HelpFragment extends Fragment {

    private View mInflatedView;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.help_view, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mButton = (Button) mInflatedView.findViewById(R.id.help_report_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html")
                        .putExtra(Intent.EXTRA_EMAIL, "zuk0.hack@gmail.com")
                        .putExtra(Intent.EXTRA_SUBJECT, "BUG REPORT")
                        .putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.help_intent_text));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.help_intent_title)));
            }
        });
    }
}
