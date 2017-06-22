package com.android.apps.listener;

import com.android.apps.fragment.BaseFragment;

/**
 * Created by root on 16-8-20.
 */
public interface OnViewPagerChildCreateListener {
    void onChildCreated(BaseFragment baseFragment, int position);
}
