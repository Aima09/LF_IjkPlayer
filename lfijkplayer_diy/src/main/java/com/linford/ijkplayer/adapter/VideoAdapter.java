package com.linford.ijkplayer.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linford.ijkplayer.R;
import com.linford.ijkplayer.entity.VideoInfo;
import com.linford.ijkplayer.view.GlideApp;

import java.util.List;


/**
 * Created by LinFord on 2017/12/22 .
 * 视频列表
 */

public class VideoAdapter extends BaseQuickAdapter<VideoInfo> {

    private Context mContext;
    public VideoAdapter(int layoutResId, List<VideoInfo> data, Context context) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override protected void convert(BaseViewHolder baseViewHolder, VideoInfo media) {
        baseViewHolder.setText(R.id.video_name,media.getTitle());
        GlideApp.with(mContext).load(media.getThumbPath()).transition(DrawableTransitionOptions.withCrossFade()).error(R.mipmap.video_cover).into((ImageView) baseViewHolder.getView(R.id.video_cover_image));

    }
}
