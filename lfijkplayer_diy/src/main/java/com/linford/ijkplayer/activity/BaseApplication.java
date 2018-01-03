package com.linford.ijkplayer.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by sujuntao on 2017/11/28 .
 * MultiDexApplication
 *
 */
public abstract class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication instance;

    public static BaseApplication getContext() {
        return instance;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        //加载so文件
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses("com.linford.ijkplayer");
        }


    }

    public abstract Handler getHandler();

    public abstract  int getMainThreadId();
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
