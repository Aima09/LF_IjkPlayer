package com.yf.linford.ijkplayer.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.yf.linford.ijkplayer.R;
import com.yf.linford.ijkplayer.entity.VideoInfo;
import com.yf.linford.ijkplayer.listener.OnShowThumbnailListener;
import com.yf.linford.ijkplayer.util.GlideApp;
import com.yf.linford.ijkplayer.util.MediaUtils;
import com.yf.linford.ijkplayer.widget.PlayStateParams;
import com.yf.linford.ijkplayer.widget.PlayerView;

import butterknife.ButterKnife;

public class VideoPlayActivity extends AppCompatActivity {

    //@BindView(R.id.video_view) LFIjkPlayer mVideoIjkplayer;
     private PlayerView player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
      //  mVideoIjkplayer=findViewById(R.id.video_ijkplayer);

        initVideoPlay();
    }

    private void initVideoPlay() {
        String url = "http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4";

        final VideoInfo videoInfo = getIntent().getParcelableExtra("videoInfo");
       // mVideoIjkplayer.setVideoPath(videoInfo.getPath());
          // GlideApp.with(this).load(videoInfo.getThumbPath()).into(player.mPlayerThumb);    // 显示界面图
        // GlideApp.with(this).load(videoInfo.getThumbPath()).into(playerView.mPlayerThumb);    // 显示界面图

       player = new PlayerView(this)
                .setTitle(videoInfo.getTitle())
                .setScaleType(PlayStateParams.fitxy)
                .hideMenu(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        GlideApp.with(VideoPlayActivity.this)
                                //视频封面
                                .load(videoInfo.getThumbPath())
                                //占位图
                                .placeholder(R.color.colorAccent)
                                //加载失败页面
                                .error(R.color.simple_player_stream_name_normal)
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(videoInfo.getPath())
                //设置自动重连的模式或者重连时间，isAuto true 出错重连，false出错不重连，connectTime重连的时间
                .setAutoReConnect( true, 5000)
                .startPlay();
        //  player.setOnInfoListener(VideoPlayerActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        //MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(VideoPlayActivity.this, false);
        /**demo的内容，激活设备常亮状态*/
        //if (wakeLock != null) {
        //    wakeLock.acquire();
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        /**demo的内容，恢复设备亮度状态*/
        //if (wakeLock != null) {
        //    wakeLock.release();
        //}
    }


}
