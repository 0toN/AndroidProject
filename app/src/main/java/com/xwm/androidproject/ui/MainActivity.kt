package com.xwm.androidproject.ui

import com.xwm.androidproject.R
import com.xwm.androidproject.databinding.ActivityMainBinding
import com.xwm.base.base.BaseActivity


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun initView() {
        mDataBinding.btnTest.setOnClickListener {
            test()
        }
    }

    override fun initDataObserver() {
        mViewModel.nicknameData.observe(this, {
            mDataBinding.btnTest.text = it
        })
    }

    override fun loadData() {

    }

    private fun test() {
        mViewModel.getNickname()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

}