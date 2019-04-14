package com.xwm.androidproject.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.xwm.androidproject.R;
import com.xwm.androidproject.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private TextView mTxtTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxtTest = findViewById(R.id.tv_test);
    }
}
