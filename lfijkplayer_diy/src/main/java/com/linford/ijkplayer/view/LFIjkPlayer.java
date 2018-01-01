package com.linford.ijkplayer.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.linford.ijkplayer.listener.VideoPlayerListener;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * 普通的视频播放器
 * Created by GuoShaoHong on 2017/7/25.
 */

public class LFIjkPlayer extends FrameLayout implements MediaController.MediaPlayerControl,VideoPlayerListener{

    public static final String TAG="LFIjkPlayer";
    /**
     * 由ijkplayer提供，用于播放视频，需要给他传入一个surfaceView
     */
    private IMediaPlayer mMediaPlayer = null;

    /**
     * 视频文件地址
     */
    private String mPath = "";

    private SurfaceView surfaceView;

    private VideoPlayerListener listener;
    private Context mContext;
    /**
     * 视频旋转角度
     */
    private int mVideoRotationDegree;
    //获取缓冲进度条
    private int BufferPercentage;
    public LFIjkPlayer(@NonNull Context context) {
        super(context);
        initVideoView(context);
        createSurfaceView();
    }

    public LFIjkPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
        createSurfaceView();

    }

    public LFIjkPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
        createSurfaceView();

    }

    private void initVideoView(Context context) {
        mContext = context;
        //获取焦点，不知道有没有必要~。~
        setFocusable(true);
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        if (TextUtils.equals("", mPath)) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            mPath = path;
           // createSurfaceView();
        } else {
            //否则就直接load
            mPath = path;
            load();
        }
    }

    /**
     * 新建一个surfaceview
     */
    private void createSurfaceView() {
        //生成一个新的surface view
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(new LmnSurfaceCallback());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        surfaceView.setLayoutParams(layoutParams);

        setRotation(mVideoRotationDegree);
        this.addView(surfaceView);
    }


    /**
     * surfaceView的监听器
     */
    private class LmnSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //surfaceview创建成功后，加载视频
            load();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    /**
     * 加载视频
     */
    private void load() {
        //每次都要重新创建IMediaPlayer
        createPlayer();
        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //给mediaPlayer设置视图
        mMediaPlayer.setDisplay(surfaceView.getHolder());

        mMediaPlayer.prepareAsync();
    }

    /**
     * 创建一个新的player
     */
    private void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

//        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        //设置视频倍速,解决音画不同步问题
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);

        mMediaPlayer = ijkMediaPlayer;

        if (listener != null) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
        }
    }

    public SurfaceView getSurfaceView(){
        return surfaceView;
    }

    public IMediaPlayer getIjkMediaPlayer(){
        return mMediaPlayer;
    }
    //////////////////////////////屏幕旋转的实现/////////////////////////////////////////////////


    public void setVideoPlayerListener(VideoPlayerListener listener) {
        this.listener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
        }
    }

    /**
     * -------======--------- 下面封装了一下控制视频的方法
     */

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 是否正在播放
     * @return
     */
    public boolean isPlaying() {
        return mMediaPlayer!=null?mMediaPlayer.isPlaying():false;
    }

    @Override public int getBufferPercentage() {
        return BufferPercentage;
    }

    @Override public boolean canPause() {
        return false;
    }

    @Override public boolean canSeekBackward() {
        return false;
    }

    @Override public boolean canSeekForward() {
        return false;
    }

    @Override public int getAudioSessionId() {
        return 0;
    }

    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return (int) mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override public void seekTo(int pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }
 ////////////////////////////////////////接口实现//////////////////////////////////////////////////

    @Override public void onPrepared(IMediaPlayer mp) {

    }

    @Override public void onCompletion(IMediaPlayer mp) {

    }

    /**
     * 设置旋转角度
     */
    public void setPlayerRotation(int rotation) {
        mVideoRotationDegree = rotation;
        if (surfaceView != null) {
            surfaceView.setRotation(mVideoRotationDegree);
        }
    }
    /**
     * 返回缓冲进度
     * @param mp
     * @param percent
     */
    @Override public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        BufferPercentage=percent;
    }

    @Override public void onSeekComplete(IMediaPlayer mp) {

    }

    @Override public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {

    }

    @Override public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                break;
            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + extra);
                break;
            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                break;
            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                break;
            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                break;
            case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                break;
            case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED://获取视频旋转角度
                mVideoRotationDegree = extra;
                Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + extra);
                if (surfaceView != null)
                    surfaceView.setRotation(extra);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                break;
        }
        return true;
    }

}
