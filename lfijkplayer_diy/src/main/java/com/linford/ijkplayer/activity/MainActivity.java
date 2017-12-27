package com.linford.ijkplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linford.ijkplayer.R;
import com.linford.ijkplayer.adapter.VideoAdapter;
import com.linford.ijkplayer.entity.VideoInfo;
import com.linford.ijkplayer.utils.VideoSource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.video_list) RecyclerView mVideoList;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewListener();
    }

    public void initViewListener() {

        mVideoList = findViewById(R.id.video_list);
        /**
         * 初始化内容列表布局
         * 网格布局,设置为6列
         */
        mVideoList.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        final List<VideoInfo> videoList = new VideoSource(MainActivity.this).getSysVideoList();
        mVideoAdapter = new VideoAdapter(R.layout.item_video_list, videoList, MainActivity.this);

        mVideoList.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override public void onItemClick(View view, int i) {
                Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("videoInfo", videoList.get(i));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });


    }
}
