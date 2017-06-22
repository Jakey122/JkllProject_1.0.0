package com.android.apps.web;

/**
 * Created by root on 16-7-6.
 */
public interface ILoadUrlListener {

    void onLoadStart();

    void onLoadFinish();

    void onLoadError();
}
