package com.app.projekta.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.app.projekta.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class ActivityRtmpPlayer extends AppCompatActivity {

    private RtmpDataSourceFactory rtmpDataSourceFactory;
    private SimpleExoPlayer player;
    String url;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rtmp_player);

        url = getIntent().getStringExtra("url");
        progressBar = findViewById(R.id.progressBar);

        //Create Simple Exoplayer Player
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        PlayerView playerView = findViewById(R.id.simple_player);
        playerView.setPlayer(player);

        //Create RTMP Data Source
        rtmpDataSourceFactory = new RtmpDataSourceFactory();
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url), rtmpDataSourceFactory, extractorsFactory, null, null);

        MediaSource videoSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory).createMediaSource(Uri.parse(url));

        player.prepare(videoSource);
        player.setPlayWhenReady(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 5000);

        Log.d("INFO", "ActivityRtmpPlayer");

    }

    @Override
    public void onBackPressed() {
        ActivityRtmpPlayer.this.finish();
        player.stop();
    }

}