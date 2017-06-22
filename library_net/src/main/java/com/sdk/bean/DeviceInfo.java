package com.sdk.bean;

import android.os.Parcel;

import com.sdk.parse.IParseHelper;

public class DeviceInfo extends BaseInfo {

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId, String nickname) {
        this.nickname = nickname;
        this.nickname = deviceId;
    }

    protected DeviceInfo(Parcel in) {
        super(in);
    }

    @Override
    public DeviceInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        info = helper.getInfo(DeviceInfo.class, "data");
        mList = helper.getInfoList(DeviceInfo.class, "list");
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
    }

    public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel in) {
            return new DeviceInfo(in);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
