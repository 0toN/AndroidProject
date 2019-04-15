package com.xwm.androidproject.ui

import android.os.Bundle
import android.widget.TextView

import com.xwm.androidproject.R
import com.xwm.androidproject.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mTxtTest: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTxtTest = findViewById(R.id.tv_test)
    }
}
