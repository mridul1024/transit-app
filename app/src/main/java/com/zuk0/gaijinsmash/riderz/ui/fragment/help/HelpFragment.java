package com.zuk0.gaijinsmash.riderz.ui.fragment.help;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zuk0.gaijinsmash.riderz.R;

public class HelpFragment extends Fragment {

    private View mInflatedView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflatedView = inflater.inflate(R.layout.view_help, container, false);
        return mInflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button mButton = mInflatedView.findViewById(R.id.help_report_button);
        mButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822")
                    .putExtra(Intent.EXTRA_EMAIL, new String[] {"zuk0.hack@gmail.com"})
                    .putExtra(Intent.EXTRA_SUBJECT, "***RIDERZ BUG REPORT***")
                    .putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.help_intent_text));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.help_intent_title)));
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}
