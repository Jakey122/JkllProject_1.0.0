package com.android.apps.util;

/**
 * Created by Xsl on 2016/6/29.
 */
public class PageId {


    public static final int PAGE_BASE_ID = 100;

    public static final int PAGE_AD = PAGE_BASE_ID * 1; //AD页面
    public static final int PAGE_USER = PAGE_BASE_ID * 2; //用户界面
    public static final int PAGE_MAIN = PAGE_BASE_ID * 3; //主界面
    public static final int PAGE_DEVICE = PAGE_BASE_ID * 4; //设备
    public static final int PAGE_ORDER = PAGE_BASE_ID * 5; //订单
    public static final int PAGE_MINE = PAGE_BASE_ID * 6; //我的
    public static final int PAGE_UPDATE = PAGE_BASE_ID * 7;//自身版本升级提示安装
    public static final int PAGE_WEB = PAGE_BASE_ID * 8;//WEB

    /**
     * AD页面
     */
    public static class PageAD {
        public final static int PAGE_AD_SPLASH = PAGE_AD + 1; //开屏页面
    }

    public static class PageUser {
        public static final int PAGE_LOGIN = PAGE_MAIN + 1; //登录
        public static final int PAGE_REGISTER = PAGE_MAIN + 2; //注册
        public static final int PAGE_FORGET_PASSWORD = PAGE_MAIN + 3; //忘记密码
        public static final int PAGE_RESET_PASSWORD = PAGE_MAIN + 4; //重置密码
        public static final int PAGE_RESET_PASSWORD_COMPLETE = PAGE_MAIN + 4; //重置密码成功
    }

    public static class PageMain {
        public static final int PAGE_HOME = PAGE_MAIN + 1; //首页
        public static final int PAGE_DEVICE = PAGE_MAIN + 2; //设备
        public static final int PAGE_ORDER = PAGE_MAIN + 3; //订单
        public static final int PAGE_MINE = PAGE_MAIN + 4; //我的
    }

    public static class PageDevice {
        public static final int PAGE_DEVICE_DETAIL = PAGE_DEVICE + 1; //详情
        public static final int PAGE_DEVICE_CAPTURE = PAGE_DEVICE + 2; //添加
        public static final int PAGE_DEVICE_CAPTURE_ADD_COMPLETE = PAGE_DEVICE + 3; //添加完成
        public static final int PAGE_DEVICE_PAY = PAGE_DEVICE + 4; //购买
    }

    public static class PageOrder {
        public static final int PAGE_ORDER_DETAIL = PAGE_ORDER + 1; //详情
        public static final int PAGE_ORDER_DETAIL_PAY_SUCCESS = PAGE_ORDER + 2; //支付成功
        public static final int PAGE_ORDER_DETAIL_PAY_FAIL = PAGE_ORDER + 3; //支付失败
    }

    /**
     * 我的页面
     */
    public static class PageMine {
        public static final int PAGE_MINE_QUESTION = PAGE_MINE + 1; //常见问题
        public static final int PAGE_MINE_CUSTOMER = PAGE_MINE + 2; //客服电话
        public static final int PAGE_MINE_FEEDBACK = PAGE_MINE + 3; //意见反馈
        public static final int PAGE_MINE_ABOUT = PAGE_MINE + 4;//关于我们
    }

    /**
     * 自身更新弹窗按钮
     */
    public static class PageUpdate {
        public final static int PAGE_UPDATE_CANCLE = PAGE_UPDATE + 1; //取消更新
        public final static int PAGE_UPDATE_CONFIRM = PAGE_UPDATE + 2; //确认更新
    }
}
