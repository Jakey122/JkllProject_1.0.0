package com.android.apps.bean;

import android.os.Parcel;

import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-5-27.
 */
public class LoadInfo extends BaseInfo {

    public LoadInfo(String title) {
        this.nickname = title;
    }

    protected LoadInfo(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<LoadInfo> CREATOR = new Creator<LoadInfo>() {
        @Override
        public LoadInfo createFromParcel(Parcel in) {
            return new LoadInfo(in);
        }

        @Override
        public LoadInfo[] newArray(int size) {
            return new LoadInfo[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }
}
