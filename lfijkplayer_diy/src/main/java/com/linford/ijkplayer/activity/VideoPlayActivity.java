package com.linford.ijkplayer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linford.ijkplayer.R;
import com.linford.ijkplayer.entity.VideoInfo;
import com.linford.ijkplayer.view.LFIjkPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayActivity extends AppCompatActivity {

    @BindView(R.id.ijkPlayer) LFIjkPlayer mVideoIjkplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        initViewListener();

    }
    private void initViewListener(){
        startPlay();
    }

    /**
     * 开始播放
     */
    private void startPlay(){
        final VideoInfo videoInfo = getIntent().getParcelableExtra("videoInfo");
        mVideoIjkplayer.setVideoPath(videoInfo.getPath());
    }
}