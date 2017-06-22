package com.sdk.bean;

import android.os.Parcel;

import com.sdk.parse.IParseHelper;

public class TrafficInfo extends BaseInfo {

    public TrafficInfo() {
    }

    protected TrafficInfo(Parcel in) {
        super(in);
    }

    @Override
    public TrafficInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        info = helper.getInfo(TrafficInfo.class, "data");
        mList = helper.getInfoList(TrafficInfo.class, "list");
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
    }

    public static final Creator<TrafficInfo> CREATOR = new Creator<TrafficInfo>() {
        @Override
        public TrafficInfo createFromParcel(Parcel in) {
            return new TrafficInfo(in);
        }

        @Override
        public TrafficInfo[] newArray(int size) {
            return new TrafficInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
