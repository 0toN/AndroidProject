package com.xwm.androidproject.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.xwm.androidproject.databinding.ActivityMainBinding
import com.xwm.androidproject.util.InjectorUtil
import com.xwm.base.base.BaseActivity


class MainActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getMainModelFactory()).get(MainViewModel::class.java)
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnTest.setOnClickListener {
            viewModel.getName()
        }
    }
}