package com.linford.lfijkplayer.activity;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.linford.lfijkplayer.entity.VideoInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LinFord on 2017/12/22 .
 */

public class VideoSource {
    private Cursor cursor;
    private List<VideoInfo> sysVideoList = new ArrayList<VideoInfo>();
    private Context mContext;
    public VideoSource(Context context) {
        this.mContext=context;
        setVideoList();
    }

    public List<VideoInfo> getSysVideoList(){
        return  sysVideoList;
    }
    private void setVideoList() {
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };

        cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if(cursor==null){
            //Toast.makeText(SystemVideoChooseActivity.this, "没有找到可播放视频文件", 1).show();
            return;
        }
        if (cursor.moveToFirst()) {
            do {
                VideoInfo info = new VideoInfo();
                int videoId = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));

                Cursor thumbCursor = mContext.getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + videoId, null, null);
                if (thumbCursor.moveToFirst()) {
                    info.setThumbPath(thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }

                info.setPath(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                info.setTitle(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));

                info.setDisPlayName(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
               // LogUtil.log(TAG, "DisplayName:"+info.getDisplayName());
                info.setMimeType(cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));



                sysVideoList.add(info);
            }
            while (cursor.moveToNext());
        }
    }
}
