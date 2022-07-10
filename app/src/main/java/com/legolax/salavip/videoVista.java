package com.legolax.salavip;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URI;

public class videoVista extends AppCompatActivity {

    VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_vista);

        video = (VideoView) findViewById(R.id.videoViewid);
        String uriVideo;
        uriVideo = getIntent().getStringExtra("video");
        video.setVideoURI(Uri.parse(uriVideo));

        MediaController mediaController = new MediaController(this);
        video.setMediaController(mediaController);

        video.start();

    }

}