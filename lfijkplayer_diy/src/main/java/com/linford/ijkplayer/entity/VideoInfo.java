package com.linford.ijkplayer.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LinFord on 2017/12/22 .
 */

public class VideoInfo implements Parcelable {
    private String ThumbPath;
    private String Path;
    private String Title;
    private String DisPlayName;
    private String MimeType;

    public VideoInfo() {
    }

    public VideoInfo(String thumbPath, String path, String title, String disPlayName, String mimeType) {
        ThumbPath = thumbPath;
        Path = path;
        Title = title;
        DisPlayName = disPlayName;
        MimeType = mimeType;
    }

    protected VideoInfo(Parcel in) {
        ThumbPath = in.readString();
        Path = in.readString();
        Title = in.readString();
        DisPlayName = in.readString();
        MimeType = in.readString();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    public String getThumbPath() {
        return ThumbPath;
    }

    public void setThumbPath(String thumbPath) {
        ThumbPath = thumbPath;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDisPlayName() {
        return DisPlayName;
    }

    public void setDisPlayName(String disPlayName) {
        DisPlayName = disPlayName;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    @Override public String toString() {
        return "VideoInfo{" +
                "ThumbPath='" + ThumbPath + '\'' +
                ", Path='" + Path + '\'' +
                ", Title='" + Title + '\'' +
                ", DisPlayName='" + DisPlayName + '\'' +
                ", MimeType='" + MimeType + '\'' +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ThumbPath);
        parcel.writeString(Path);
        parcel.writeString(Title);
        parcel.writeString(DisPlayName);
        parcel.writeString(MimeType);
    }
}
