package com.linford.ijkplayer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.linford.ijkplayer.R;
import com.linford.ijkplayer.entity.VideoInfo;
import com.linford.ijkplayer.manager.IjkPlayerManager;

import butterknife.ButterKnife;

public class VideoPlayActivity2 extends AppCompatActivity {
//    @BindView(R.id.ijkPlayer) IjkVideoView mIjkPlayer;
    private IjkPlayerManager mIjkPlayerManager;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play2);
        ButterKnife.bind(this);
        initVideoPlay();
    }

    private void initVideoPlay() {
        mIjkPlayerManager = new IjkPlayerManager(VideoPlayActivity2.this);
        mGestureDetector = mIjkPlayerManager.getGestureDetector();
        final VideoInfo videoInfo = this.getIntent().getParcelableExtra("videoInfo");
//        mIjkPlayer.setVideoPath(videoInfo.getPath());
//        mIjkPlayer.start();
        mIjkPlayerManager.setVideoPath(videoInfo.getPath());
        mIjkPlayerManager.setVideoTitle(videoInfo.getTitle());
        mIjkPlayerManager.startPlay();
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                mIjkPlayerManager.endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }
}

