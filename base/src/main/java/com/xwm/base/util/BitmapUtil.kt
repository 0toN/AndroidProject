package com.xwm.base.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.*
import java.nio.ByteBuffer


object BitmapUtil {
    private val TAG = "BitmapUtil"

    /**
     * 把Bitmap写入到sdcard中
     *
     * @param bitmap
     * @param savePath
     */
    fun saveBitmap(bitmap: Bitmap, savePath: String) {
        // 判断是否可以对SdCard进行操作
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            //目录转化成文件夹
            val dirFile = File(savePath)
            if (!dirFile.exists()) {
                //如果不存在，那就建立这个文件夹
                dirFile.mkdirs()
            }
            //文件夹有啦，就可以保存图片啦
            // 在SdCard的目录下创建图片文,以当前时间为其命名
            val file = File(savePath)

            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                out?.flush()
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 按质量压缩
     *
     * @param bitmap         源图片
     * @param limitSize 限制的图片大小，单位kb
     * @return 压缩过的图片
     */
    fun compressByQuality(bitmap: Bitmap, limitSize: Int, recycle: Boolean = false): Bitmap {
        val baos = ByteArrayOutputStream()
        var quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)

        val maxByteSize = limitSize * 1024
        //循环判断如果压缩后图片是否大于maxByteSize，大于继续压缩
        while (baos.toByteArray().size > maxByteSize && quality > 0) {
            //清空baos
            baos.reset()
            //每次都减少5
            quality -= 5
            // 把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        }
        val bytes = baos.toByteArray()
        if (recycle && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * 按采样大小压缩
     *
     * @param bitmap        源图片
     * @param sampleSize 采样率大小
     * @return 按采样率压缩后的图片
     */
    fun compressBySampleSize(bitmap: Bitmap, sampleSize: Int, recycle: Boolean = false): Bitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = sampleSize
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val bytes = baos.toByteArray()
        if (recycle && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    fun decodeScaledBitmap(bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, reqWidth,
                reqHeight, true)
    }

    fun decodeSampledBitmap(bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
        val byteArray = bitmap2ByteArray(bitmap)

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
    }

    fun decodeSampledBitmap(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun bitmap2ByteArray(bitmap: Bitmap): ByteArray {
        val bytes = bitmap.byteCount
        val byteBuffer = ByteBuffer.allocate(bytes)
        bitmap.copyPixelsToBuffer(byteBuffer)
        return byteBuffer.array()
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (reqWidth == 0 || reqHeight == 0) {
            return inSampleSize
        }
        val height = options.outHeight
        val width = options.outWidth
        LogUtil.d(TAG, "origin, w = $width h = $height")

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        LogUtil.d(TAG, "sampleSize = $inSampleSize")
        return inSampleSize
    }
}
