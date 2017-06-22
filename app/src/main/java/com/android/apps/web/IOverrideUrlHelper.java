package com.android.apps.web;

import android.webkit.WebView;

/**
 * Created by root on 16-7-6.
 */
public interface IOverrideUrlHelper {

    boolean handleOverrideUrl(WebView view, String url);
}
