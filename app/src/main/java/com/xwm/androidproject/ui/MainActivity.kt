package com.xwm.androidproject.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.xwm.androidproject.databinding.ActivityMainBinding
import com.xwm.androidproject.net.Network
import com.xwm.base.base.BaseActivity
import com.xwm.base.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : BaseActivity() {

    private val network by lazy { Network.instance }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTest.setOnClickListener {
//            launch {
//                val bitmap = suspendLoadImage()
//                bitmap?.let {
//                    val bitmap1 = suspendCropBitmap(it, 2, 2, 0, 0)
//                    val bitmap2 = suspendCropBitmap(it, 3, 3, 2, 2)
//                    binding.iv1.setImageBitmap(bitmap1)
//                    binding.iv2.setImageBitmap(bitmap2)
//                }
//            }
            launch {
                val testBean = network.test()
                LogUtil.e("xxxxxxx testBEan  =" + testBean.data.name)
            }
        }
    }

    private suspend fun suspendLoadImage() =
            withContext(Dispatchers.IO) {
                val imgUrl = "http://oss.tuyuing.com/TUYU/trend/20190930/trend257401569854904487.jpeg"
                val connection = URL(imgUrl).openConnection() as HttpURLConnection
                if (connection.responseCode != 200) {
                    return@withContext null
                }
                return@withContext BitmapFactory.decodeStream(connection.inputStream)
            }

    private suspend fun suspendCropBitmap(source: Bitmap, sliceX: Int, sliceY: Int, posX: Int, posY: Int) =
            withContext(Dispatchers.Default) {
                val cropWidth = source.width / sliceX
                val cropHeight = source.height / sliceY
                Bitmap.createBitmap(source, cropWidth * posX, cropHeight * posY, source.width / sliceX,
                        source.height / sliceY)
            }
}