package com.example.grievanceregistrationform;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.potyvideo.library.AndExoPlayerView;

public class VideoPlay extends AppCompatActivity {

    private AndExoPlayerView andExoPlayerView;

    private String TEST_URL_MP4 = "http://climatesmartcity.com/UBA/help/loginhelp.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        andExoPlayerView = findViewById(R.id.andExoPlayerView);

loadMP4ServerSide();

    }


    private void loadMP4ServerSide() {
        andExoPlayerView.setSource(TEST_URL_MP4);
    }




}
