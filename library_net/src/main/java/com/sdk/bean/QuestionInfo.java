package com.sdk.bean;

import android.os.Parcel;

import com.sdk.bean.BaseInfo;
import com.sdk.parse.IParseHelper;

public class QuestionInfo extends BaseInfo {

    public QuestionInfo() {
    }

    protected QuestionInfo(Parcel in) {
        super(in);
    }

    @Override
    public QuestionInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        mList = helper.getInfoList(QuestionInfo.class, "list");
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        super.writeToParcel(dest, flags);
    }

    public static final Creator<QuestionInfo> CREATOR = new Creator<QuestionInfo>() {
        @Override
        public QuestionInfo createFromParcel(Parcel in) {
            return new QuestionInfo(in);
        }

        @Override
        public QuestionInfo[] newArray(int size) {
            return new QuestionInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
