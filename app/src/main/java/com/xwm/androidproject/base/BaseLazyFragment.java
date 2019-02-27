package com.xwm.androidproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author Created by Adam on 2019-02-20
 */
public abstract class BaseLazyFragment extends BaseFragment {
    // 是否进行过懒加载
    private boolean isLazyLoad;
    // Fragment 是否可见
    private boolean isFragmentVisible;

    /**
     * 当前 Fragment 是否可见
     */
    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisible) {
            initLazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isFragmentVisible = isVisibleToUser;
        if (isVisibleToUser && getView() != null) {
            if (!isLazyLoad) {
                initLazyLoad();
            } else {
                // 从不可见到可见
                onRestart();
            }
        }
    }

    /**
     * 初始化懒加载
     */
    protected void initLazyLoad() {
        if (!isLazyLoad) {
            isLazyLoad = true;
            initFragment();
        }
    }

    //重置 LazyLoad 的状态
    protected void resetLazyLoadStatus() {
        this.isLazyLoad = false;
    }

    /**
     * 是否进行了懒加载
     */
    protected boolean isLazyLoad() {
        return isLazyLoad;
    }

    /**
     * 跟 Activity 的同名方法效果一样
     */
    protected void onRestart() {
        // 从可见的状态变成不可见状态，再从不可见状态变成可见状态时回调
    }

    protected void initFragment() {
        initVar();
        initView();
        loadData();
    }

    //初始化变量
    protected abstract void initVar();

    //初始化控件
    protected abstract void initView();

    //初始化数据
    protected abstract void loadData();
}
