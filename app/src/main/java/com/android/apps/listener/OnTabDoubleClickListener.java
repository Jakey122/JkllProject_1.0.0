package com.android.apps.listener;

import android.support.design.widget.TabLayout;

/**
 * Created by root on 16-8-21.
 */
public interface OnTabDoubleClickListener extends TabLayout.OnTabSelectedListener {

    void onTabDoubleClick(TabLayout.Tab tab);
}
