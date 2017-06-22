package com.sdk.bean;

import android.os.Parcel;

import com.sdk.bean.BaseInfo;
import com.sdk.parse.IParseHelper;

public class DeviceDetailInfo extends BaseInfo {

    public DeviceDetailInfo() {
    }

    protected DeviceDetailInfo(Parcel in) {
        super(in);
    }

    @Override
    public DeviceDetailInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        info = helper.getInfo(DeviceDetailInfo.class, "data");
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
    }

    public static final Creator<DeviceDetailInfo> CREATOR = new Creator<DeviceDetailInfo>() {
        @Override
        public DeviceDetailInfo createFromParcel(Parcel in) {
            return new DeviceDetailInfo(in);
        }

        @Override
        public DeviceDetailInfo[] newArray(int size) {
            return new DeviceDetailInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
