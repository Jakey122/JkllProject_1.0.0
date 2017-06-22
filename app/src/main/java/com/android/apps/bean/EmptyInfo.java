package com.android.apps.bean;

import android.os.Parcel;

import com.sdk.bean.BaseInfo;

/**
 * Created by root on 16-5-23.
 */
public class EmptyInfo extends BaseInfo {

    public static final int EMPTY_TYPE_DEFAULT = 0;

    private int mType;

    public EmptyInfo() {
        mType = EMPTY_TYPE_DEFAULT;
    }

    public EmptyInfo(int type) {
        mType = type;
    }

    protected EmptyInfo(Parcel in) {
        super(in);
        mType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
        dest.writeInt(mType);
    }

    public static final Creator<EmptyInfo> CREATOR = new Creator<EmptyInfo>() {
        @Override
        public EmptyInfo createFromParcel(Parcel in) {
            return new EmptyInfo(in);
        }

        @Override
        public EmptyInfo[] newArray(int size) {
            return new EmptyInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getEmptyType() {
        return mType;
    }
}
