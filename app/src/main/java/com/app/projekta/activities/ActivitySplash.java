package com.app.projekta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.app.projekta.Config;
import com.app.projekta.R;
import com.google.android.gms.ads.InterstitialAd;

public class ActivitySplash extends AppCompatActivity {

    Boolean isCancelled = false;
    private ProgressBar progressBar;
    private InterstitialAd interstitialAd;
    String id = "0", cname = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("nid")) {
            id = getIntent().getStringExtra("nid");
            cname = getIntent().getStringExtra("cname");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCancelled) {
                    if (id.equals("0")) {
                        Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(ActivitySplash.this, ActivityOneSignalDetail.class);
                        intent.putExtra("id", id);
                        intent.putExtra("cname", cname);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, Config.SPLASH_TIME);

    }

}
