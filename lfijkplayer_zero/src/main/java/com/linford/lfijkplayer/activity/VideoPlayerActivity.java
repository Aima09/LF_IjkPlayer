package com.linford.lfijkplayer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linford.lfijkplayer.R;
import com.linford.lfijkplayer.entity.VideoInfo;
import com.linford.lfijkplayer.view.LFIjkPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity {

    @BindView(R.id.video_ijkplayer) LFIjkPlayer mVideoIjkplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        final VideoInfo videoInfo = getIntent().getParcelableExtra("videoInfo");
        mVideoIjkplayer.setVideoPath(videoInfo.getPath());
//        GlideApp.with(this).load(videoInfo.getThumbPath()).into(player.mPlayerThumb);    // 显示界面图
//        GlideApp.with(this).load(videoInfo.getThumbPath()).into(playerView.mPlayerThumb);    // 显示界面图

    }
}
