package com.sdk.bean;

import com.sdk.bean.BaseInfo;
import com.sdk.parse.IParseHelper;

/**
 * Created by xiesongrin on 17/5/4.
 */

public class UserInfo extends BaseInfo {

    @Override
    public UserInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        return this;
    }
}
