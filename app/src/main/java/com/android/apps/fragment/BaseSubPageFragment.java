package com.android.apps.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

/**
 */
public class BaseSubPageFragment extends Fragment {
    public ViewPager mViewPager;

    public BaseSubPageFragment() {
        // TODO Auto-generated constructor stub
    }

    public int getCurrentItem() {
        return 0;
    }

    public void openDefaultPage() {

    }
}
