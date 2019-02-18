package com.xwm.androidproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Created by Adam on 2019-02-15
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (regEvent() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 需要注册EventBus，则重写该方法 并返回 true
     */
    protected boolean regEvent() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (regEvent()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
