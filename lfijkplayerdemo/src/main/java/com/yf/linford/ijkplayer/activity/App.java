package com.yf.linford.ijkplayer.activity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.support.multidex.MultiDex;


import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static Handler handler;
    private static int mainThreadId;


    @Override public void onCreate() {
        //loadProperti();
        initStaticParam();
        //初始化图片加载框架
        initImageLoder();
        initIjkPlayer();
        super.onCreate();
        //LeakCanary会自动去分析当前的内存状态，如果检测到泄漏会发送到通知栏，点击通知栏就可以跳转到具体的泄漏分析页面。
        //测试的使用,上线的时候移除,否则会生成一个app

        //greenDao
        //setDatabase();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


    public void initStaticParam() {
        handler = new Handler();
        mainThreadId = Process.myTid();
    }

    private void initImageLoder() {
        //Fresco
//        Fresco.initialize(this);
    }

    public Handler getHandler() {
        return handler;
    }


    /**
     * 初始化视频播放器
     */
    private void initIjkPlayer(){
        //加载so文件
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            return;
        }
    }




}