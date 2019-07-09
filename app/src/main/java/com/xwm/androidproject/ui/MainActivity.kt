package com.xwm.androidproject.ui

import android.os.Bundle
import com.xwm.androidproject.R
import com.xwm.base.base.BaseActivity
import com.xwm.base.util.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTest.setOnClickListener {
            ToastUtil.showShort("Hello World!")
        }
    }
}