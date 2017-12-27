package com.linford.ijkplayer.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linford.ijkplayer.R;
import com.linford.ijkplayer.entity.VideoInfo;
import com.linford.ijkplayer.listener.VideoPlayerListener;
import com.linford.ijkplayer.utils.Strings;
import com.linford.ijkplayer.utils.ToastUtil;
import com.linford.ijkplayer.view.LFIjkPlayer;
import com.linford.ijkplayer.view.VerticalSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VideoPlayActivity extends AppCompatActivity implements VideoPlayerListener, SeekBar.OnSeekBarChangeListener, AudioManager.OnAudioFocusChangeListener {


    @BindView(R.id.ijkPlayer)
    LFIjkPlayer mVideoIjkplayer;

    //顶部控制栏控件
    @BindView(R.id.ijkplayer_top_bar)
    LinearLayout mIjkplayerTopBar;
    @BindView(R.id.app_video_title)
    TextView mAppVideoTitle;
    //中间控件
    @BindView(R.id.play_icon)
    ImageView mPlayIcon;
    @BindView(R.id.app_video_box)
    RelativeLayout mAppVideoBox;
    @BindView(R.id.iv_trumb)
    ImageView mIvTrumb;
    @BindView(R.id.volume_controller_container)
    LinearLayout volumeControllerContainer;
    @BindView(R.id.brightness_controller_container)
    LinearLayout brightnessControllerContainer;
    @BindView(R.id.volume_controller_seekBar)
    VerticalSeekBar volumeControllerSeekBar;
    @BindView(R.id.brightness_controller_seekbar)
    VerticalSeekBar brightnessControllerSeekbar;

    //底部控制栏控件
    @BindView(R.id.ijkplayer_bottom_bar)
    LinearLayout mIjkplayerBottomBar;
    @BindView(R.id.app_video_play)
    ImageView mAppVideoPlay;
    @BindView(R.id.app_video_seekBar)
    SeekBar mAppVideoSeekBar;
    @BindView(R.id.app_video_currentTime)
    TextView mAppVideoCurrentTime;
    @BindView(R.id.app_video_endTime)
    TextView mAppVideoEndTime;


    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;

    private AudioManager mAudioManager;
    private GestureDetector mGestureDetector;

    //视频填充样式
    private static final int FILL_MODE_ADAPT = 0;
    public static final int FILL_MODE_FILL = 1;
    public static final int FILL_MODE_STRETCH = 2;
    public static final int FILL_MODE_CENTER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        initViewListener();

    }

    /**
     * 初始化视图和监听器
     */
    private void initViewListener() {
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        startPlay();
        //处理控制栏的显示
        initControllView();
        //注册监听事件
        mVideoIjkplayer.setVideoPlayerListener(this);
        mAppVideoSeekBar.setOnSeekBarChangeListener(this);
        mGestureDetector = new GestureDetector(this, new VideoPlayActivity.MyGestureListener());

    }

    /**
     * 开始播放
     */
    private void startPlay() {
        final VideoInfo videoInfo = getIntent().getParcelableExtra("videoInfo");
        mVideoIjkplayer.setVideoPath(videoInfo.getPath());
        mAppVideoTitle.setText(videoInfo.getTitle());
        //  GlideApp.with(this).asBitmap().load(videoInfo.getThumbPath()).into(mIvTrumb);
        //获取音源焦点
        initAudioFocus();
    }

    /***
     * 暂停后启动播放
     */
    private void MediaStart() {
        mVideoIjkplayer.start();
        mHandler.sendEmptyMessage(1);
        //初始化视频进度条和文本显示
        mAppVideoSeekBar.setMax((int) mVideoIjkplayer.getDuration());
        mAppVideoSeekBar.setProgress((int) mVideoIjkplayer.getCurrentPosition());
        mAppVideoEndTime.setText(Strings.millisToText(mVideoIjkplayer.getDuration()));
        mAppVideoCurrentTime.setText(Strings.millisToText(mVideoIjkplayer.getCurrentPosition()));

    }

    ////////////////////////////////显示顶部和底部控制栏//////////////////////////////////////////
    private void initControllView() {
        mIjkplayerBottomBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                delay6Second();
                return true;
            }
        });
        delay6Second();

    }

    /**
     * 控制栏的显示与消失
     *
     * @param state
     */
    private void refreshVideoControlUI(int state) {
        mIjkplayerTopBar.setVisibility(state);
        mIjkplayerBottomBar.setVisibility(state);
        mPlayIcon.setVisibility(state);
        if (mIjkplayerTopBar.getVisibility() == View.VISIBLE) {
            mAppVideoPlay.requestFocus();
        }
    }

    /**
     * 根据发送过来的参数显示控制栏
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    refreshVideoControlUI(View.INVISIBLE);
                    break;
                case 1:
                    if (mVideoIjkplayer.isPlaying()) {
                        updateTime();
                        mAppVideoPlay.setImageResource(R.drawable.simple_player_icon_media_pause);
                        mPlayIcon.setImageResource(R.drawable.simple_player_center_pause);
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
                case 2:
                    if (mVideoIjkplayer.isPlaying()) {
                        mAppVideoPlay.setImageResource(R.drawable.simple_player_icon_media_pause);
                        mPlayIcon.setImageResource(R.drawable.simple_player_center_pause);
                    } else {
                        mPlayIcon.setImageResource(R.drawable.simple_player_center_play);
                        mAppVideoPlay.setImageResource(R.drawable.simple_player_arrow_white_24dp);
                    }
                    break;


            }
        }

        ;
    };

    /**
     * 控制栏延迟6秒后消失
     */
    private void delay6Second() {
        mHandler.removeMessages(0);
        //六秒后隐藏
        mHandler.sendEmptyMessageDelayed(0, 6000);
    }

    ///////////////////////////////////////////控制栏的显示与消失///////////////////////end////////////////////////////////////////


    ///////////////////////////////音源焦点////////////////////////////////////////////////////////

    /**
     * 处理音频相关的操作:
     */
    private void initAudioFocus() {
        //申请音频焦点
        mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    /**
     * 实现音源焦点接口
     *
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // 长久的失去音频焦点，释放MediaPlayer
                mVideoIjkplayer.stop();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // 暂时失去音频焦点，暂停播放等待重新获得音频焦点
                mVideoIjkplayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
        }
    }

    ///////////////////////////////音源焦点/////////////////////end///////////////////////////////////

   /* */

    /**
     * 处理视频填充样式
     *
     * @param mode 适应,填充,拉伸,局中
     *//*
    private void initViewMode(int mode){
        switch (mode) {
            case FILL_MODE_ADAPT://适应
                if ((float) vWidth / vHeight > (float) width / height) {
                    //视屏的高不足以填充屏幕，宽度填充，计算合适的高度
                    params.width = width;
                    params.height = width * vHeight / vWidth;
                } else {
                    //视屏的宽不足以填充屏幕，高度填充，计算合适的宽度
                    params.width = height * vWidth / vHeight;
                    params.height = height;
                }
                break;
            case FILL_MODE_FILL://填充
                if ((float) vWidth / vHeight > (float) width / height) {
                    //视屏的高不足以填充屏幕，宽度填充，舍弃部分宽度，高度填充
                    params.width = height * vWidth / vHeight;
                    params.height = height;
                } else {
                    //视屏的宽不足以填充屏幕，高度填充，舍弃部分高度，宽度度填充
                    params.width = width;
                    params.height = width * vHeight / vWidth;
                }
                break;
            case FILL_MODE_STRETCH://拉伸
                //不做任何处理就是拉伸
                break;
            case FILL_MODE_CENTER://居中
                params.width = vWidth;
                params.height = vHeight;
                break;
        }
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);

        }
        //取消音源焦点
        mAudioManager.abandonAudioFocus(this);

    }

    /**
     * 更新进度条
     */
    private void updateTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mVideoIjkplayer.isPlaying()) {
                    int position = (int) mVideoIjkplayer.getCurrentPosition();
                    mAppVideoSeekBar.setProgress(position);
                    //mTipsProgress.setProgress(currentProgress);
                    //显示当前视频进度文本
                    mAppVideoCurrentTime.setText(Strings.millisToText(position));
                }
            }
        });
    }

    @OnClick({R.id.app_video_play, R.id.app_video_box, R.id.play_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.app_video_play:
                if (mVideoIjkplayer.isPlaying()) {
                    mVideoIjkplayer.pause();
                } else {
                    mVideoIjkplayer.start();
                }
                mHandler.sendEmptyMessage(2);
                break;
            case R.id.app_video_box:
                refreshVideoControlUI(mIjkplayerBottomBar.getVisibility() == View.VISIBLE ? View.INVISIBLE
                        : View.VISIBLE);
                break;
            case R.id.play_icon:
                if (mVideoIjkplayer.isPlaying()) {
                    mVideoIjkplayer.pause();
                } else {
                    mVideoIjkplayer.start();
                }
                mHandler.sendEmptyMessage(2);
                break;
        }
    }

    /////////////////////////////////Ijkplayer播放器回调的监听////////////////////////////////////////////////
    @Override
    public void onPrepared(IMediaPlayer mp) {
        //每隔0.5秒更新视屏界面信息，如进度条，当前播放时间点等等
        // startPlay();
        MediaStart();
    }


    @Override
    public void onCompletion(IMediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {

    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {

    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        return false;
    }

    /////////////////////////////////Ijkplayer播放器回调的监听//////////////////////end////////////////////////

    ///////////////////////////////进度条监听事件////////////////////////////////////start/////////////////////
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.app_video_seekBar:
                mVideoIjkplayer.seekTo(progress);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.app_video_seekBar:
                mVideoIjkplayer.seekTo(seekBar.getProgress());
                break;
        }
    }

    ///////////////////////////////进度条监听事件//////////////////////////////////end////////////////////////////

    ///////////////////////////////手势滑动监听////////////////////////////////////start//////////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ToastUtil.showShortToastCenter("23333");
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 手势结束
     */

    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

       /* *//**//** 双击 *//**//*
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if(mDisplayAspectRatio == PLVideoTextureView.ASPECT_RATIO_FIT_PARENT){
                mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT;
            }else {
                mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT;
            }

            if (mVideoView != null) mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
//            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
//                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
//            else
//                mLayout++;
//            if (mVideoView != null)
//                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }*/

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 定时隐藏
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            volumeControllerContainer.setVisibility(View.GONE);
            brightnessControllerContainer.setVisibility(View.GONE);
        }
    };

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            //mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            volumeControllerContainer.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
       /* ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);*/
        volumeControllerSeekBar.setMax(mMaxVolume);
        volumeControllerSeekBar.setProgress(mVolume);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            // mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            brightnessControllerContainer.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        try {
            int maxBrightvalue=Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
            brightnessControllerSeekbar.setMax(maxBrightvalue);
            brightnessControllerSeekbar.setProgress((int) mBrightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
       /* ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);*/
    }
    ///////////////////////////////手势滑动监听////////////////////////////////////end//////////////////////

}