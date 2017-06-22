package com.sdk.bean;

import com.sdk.parse.IParseHelper;

/**
 * Created by xiesongrin on 17/5/4.
 *
 "cversion":"i1.0.0", //根据上传上来的版本号，判断，返回的是i1.0.1或者是a1.0.0，如果客户端的版本和返回的版本一样，就不用做处理
 "sdesc":"升级信息描述",
 "fsize": 123456, //包大小
 "apk":"http://kdjljfdkjfkl.ljdkfjldf/a.apk" //包地址，ios是跳转地址

 */

public class AppInfo extends BaseInfo {

    @Override
    public AppInfo parse(Object obj) {
        IParseHelper helper = getParseHelper(obj);
        parseDefault(helper);
        cversion = helper.getCversion();
        cversionCode = helper.getCversionCode();
        sdesc = helper.getSdesc();
        fsize = helper.getFsize();
        apk = helper.getApk();
        return this;
    }
}
