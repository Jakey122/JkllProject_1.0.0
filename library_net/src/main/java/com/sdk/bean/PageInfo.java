package com.sdk.bean;

import android.os.Parcel;

import com.sdk.parse.IParseHelper;

/**
 * Created by root on 16-5-16.
 */
public class PageInfo extends BaseInfo {

    private int totalItemCount;
    private int totalPageCount;
    private int pageSize;
    private int currentPageIndex;

    public PageInfo() {
    }

    public PageInfo(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    protected PageInfo(Parcel in) {
        super(in);
        totalItemCount = in.readInt();
        totalPageCount = in.readInt();
        pageSize = in.readInt();
        currentPageIndex = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(totalItemCount);
        dest.writeInt(totalPageCount);
        dest.writeInt(pageSize);
        dest.writeInt(currentPageIndex);
    }

    @Override
    public PageInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        totalItemCount = helper.getPageTotalItemCount();
        totalPageCount = helper.getPageTotalPageCount();
        pageSize = helper.getPageEveryPageSize();
        currentPageIndex = helper.getPageCurrentIndex();
        return this;
    }

    public PageInfo parseEnd() {
        currentPageIndex = 1;
        totalPageCount = 0;
        return this;
    }

    public static final Creator<PageInfo> CREATOR = new Creator<PageInfo>() {
        @Override
        public PageInfo createFromParcel(Parcel in) {
            return new PageInfo(in);
        }

        @Override
        public PageInfo[] newArray(int size) {
            return new PageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int getPageTotalItemCount() {
        return totalItemCount;
    }

    @Override
    public int getPageTotalPageCount() {
        return totalPageCount;
    }

    @Override
    public int getPageEveryPageSize() {
        return pageSize;
    }

    @Override
    public int getPageCurrentIndex() {
        return currentPageIndex;
    }
}
