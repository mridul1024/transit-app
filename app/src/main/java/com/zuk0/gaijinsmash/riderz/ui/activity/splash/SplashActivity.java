package com.zuk0.gaijinsmash.riderz.ui.activity.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zuk0.gaijinsmash.riderz.data.local.database.StationDbHelper;
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize station database
        new StationsDbTask(this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private static class StationsDbTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<SplashActivity> mWeakRef;

        private StationsDbTask(SplashActivity context) {
             mWeakRef = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SplashActivity ref = mWeakRef.get();
            StationDbHelper db = new StationDbHelper(ref.getApplicationContext());
            try {
                db.initStationDb(ref.getApplicationContext());
            } catch (IOException | XmlPullParserException e) {
                Log.e("Splash Activity", e.toString());
            }
            return null;
        }
    }
}
