package com.sdk.bean;

import android.os.Parcel;

import com.sdk.parse.IParseHelper;

public class OrderInfo extends BaseInfo {

    public OrderInfo() {
    }

    protected OrderInfo(Parcel in) {
        super(in);
    }

    @Override
    public OrderInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        mList = helper.getInfoList(OrderInfo.class, "list");
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel in) {
            return new OrderInfo(in);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
