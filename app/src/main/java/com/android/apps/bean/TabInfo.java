package com.android.apps.bean;

import android.os.Parcel;

import com.sdk.bean.BaseInfo;
import com.sdk.parse.IParseHelper;

/**
 * Created by root on 16-6-29.
 */
public class TabInfo extends BaseInfo {

    private int tabId;
    private int icon;
    private String address;
    private String createTime;
    private int type;
    private int position = 2;

    public TabInfo() {
    }

    public TabInfo(int id, String name, int iconRes) {
        tabId = id;
        nickname = name;
        icon = iconRes;
    }

    protected TabInfo(Parcel in) {
        super(in);
        tabId = in.readInt();
        icon = in.readInt();
        address = in.readString();
        createTime = in.readString();
        type = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
        dest.writeInt(tabId);
        dest.writeInt(icon);
        dest.writeString(address);
        dest.writeString(createTime);
        dest.writeInt(type);
        dest.writeInt(position);
    }

    @Override
    public TabInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        tabId = helper.getTabPageId();
        position = helper.getTabPagePosition();
        address = helper.getHtmlAddress();
        return this;
    }

    public static final Creator<TabInfo> CREATOR = new Creator<TabInfo>() {
        @Override
        public TabInfo createFromParcel(Parcel in) {
            return new TabInfo(in);
        }

        @Override
        public TabInfo[] newArray(int size) {
            return new TabInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getHtmlAddress() {
        return address;
    }

    @Override
    public int getTabPageId() {
        return tabId;
    }

    @Override
    public int getTabPagePosition() {
        return position;
    }

    public int getIconRes() {
        return icon;
    }

}
