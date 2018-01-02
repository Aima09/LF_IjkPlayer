package com.linford.ijkplayer.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.linford.ijkplayer.R;
import com.linford.ijkplayer.entity.VideoInfo;
import com.linford.ijkplayer.manager.IjkPlayerManager2;
import com.linford.ijkplayer.widget.IjkVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.linford.ijkplayer.view.PlayStateParams.SCALETYPE_FITXY;

public class VideoPlayActivity3 extends AppCompatActivity {

    @BindView(R.id.ijkPlayer)
    IjkVideoView mIjkPlayer;
    private IjkPlayerManager2 mIjkPlayerManager;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play2);
        ButterKnife.bind(this);
        initVideoPlay();
    }

    private void initVideoPlay() {
        mIjkPlayerManager = new IjkPlayerManager2(VideoPlayActivity3.this);
        mGestureDetector = mIjkPlayerManager.getGestureDetector();
        final VideoInfo videoInfo = this.getIntent().getParcelableExtra("videoInfo");
        mIjkPlayer.setVideoPath(videoInfo.getPath());
        mIjkPlayerManager.setScaleType(SCALETYPE_FITXY);
        mIjkPlayer.start();
//        mIjkPlayerManager.setVideoPath(videoInfo.getPath());
//        mIjkPlayerManager.setVideoTitle(videoInfo.getTitle());
//        mIjkPlayerManager.startPlay();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mIjkPlayerManager.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIjkPlayerManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIjkPlayerManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIjkPlayerManager.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mIjkPlayerManager.onBackPressed();
    }
}
