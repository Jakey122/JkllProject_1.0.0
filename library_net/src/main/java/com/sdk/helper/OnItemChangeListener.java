package com.sdk.helper;

import com.sdk.bean.BaseInfo;

/**
 * Created by Administrator on 2016-08-17.
 */
public interface OnItemChangeListener {
    void onChange(int pageId, int status, BaseInfo info);
}
