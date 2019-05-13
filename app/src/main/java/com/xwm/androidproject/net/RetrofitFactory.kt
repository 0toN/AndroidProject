package com.xwm.androidproject.net

import android.annotation.Nullable
import android.text.TextUtils
import com.xwm.androidproject.App
import com.xwm.androidproject.util.LogUtil
import com.xwm.androidproject.util.NetworkUtil
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * @author Created by Adam
 */
class RetrofitFactory private constructor() {
    private lateinit var retrofit: Retrofit
    private var builder: Retrofit.Builder

    init {
        val cache = Cache(File(App.context.externalCacheDir, "ok-cache"), 1024 * 1024 * 30L)

        // 创建OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(HttpLoggingInterceptor { message -> LogUtil.e(TAG, message) }.setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(CacheIntercepter())
                .addNetworkInterceptor(CacheNetworkInterceptor())
                .cache(cache)
                .build()

        // 创建Retrofit实例
        builder = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        if (!TextUtils.isEmpty(ServerConfig.SERVER_MAIN)) {
            retrofit = builder.baseUrl(ServerConfig.SERVER_MAIN).build()
        }
    }

    internal class CacheNetworkInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            //无缓存，进行缓存
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                    .removeHeader("Pragma")
                    //对请求进行最大60秒的缓存
                    .addHeader("Cache-Control", "max-age=60")
                    .build()
        }
    }

    internal class CacheIntercepter : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response
            val request: Request
            if (NetworkUtil.isConnected) {
                //有网络，检查10秒内的缓存
                request = chain.request()
                        .newBuilder()
                        .cacheControl(CacheControl.Builder()
                                .maxAge(10, TimeUnit.SECONDS)
                                .build())
                        .build()
            } else {
                //无网络，检查30天内的缓存，即使是过期的缓存
                request = chain.request()
                        .newBuilder()
                        .cacheControl(CacheControl.Builder()
                                .onlyIfCached().maxStale(30, TimeUnit.DAYS)
                                .build())
                        .build()
            }
            response = chain.proceed(request)
            return response.newBuilder().build()
        }
    }

    /**
     * 根据API接口类生成API实体
     *
     * @param clazz 传入的API接口类
     * @return API实体类
     */
    fun <T> create(clazz: Class<T>): T {
        checkNotNull(retrofit, "BaseUrl not init,you should init first!")
        return retrofit.create(clazz)
    }

    /**
     * 根据API接口类生成API实体
     *
     * @param baseUrl baseUrl
     * @param clazz   传入的API接口类
     * @return API实体类
     */
    fun <T> create(baseUrl: String, clazz: Class<T>): T {
        return builder.baseUrl(baseUrl).build().create(clazz)
    }

    private fun <T> checkNotNull(@Nullable obj: T, message: String): T {
        if (obj == null) {
            throw NullPointerException(message)
        }
        return obj
    }

    private object Holder {
        val retrofitFactory = RetrofitFactory()
    }

    companion object {
        private const val TAG = "RetrofitFactory"

        private val TIME_OUT: Long = 10L

        val instance: RetrofitFactory = Holder.retrofitFactory

    }
}
