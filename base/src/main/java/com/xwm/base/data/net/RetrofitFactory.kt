package com.xwm.base.data.net

import android.text.TextUtils
import com.xwm.base.constants.Constants
import com.xwm.base.util.LogUtil
import com.xwm.base.util.NetworkUtil
import com.xwm.base.util.Utils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


val api by lazy {
    RetrofitFactory.instance.create(API.BASE_URL, ApiService::class.java)
}

suspend fun <T> execute(block: suspend () -> BaseResponse<T>): Result<T>? {
    var result: Result<T>? = null
    runCatching {
        block()
    }.onSuccess { response ->
        result = if (response.code == Constants.HTTP_CODE_REQUEST_SUCCESS) {
            Result.Success(response.data)
        } else {
            Result.Failure(response.msg)
        }
    }.onFailure {
        result = Result.Error(it)
    }
    return result
}

class RetrofitFactory private constructor() {
    private lateinit var retrofit: Retrofit
    private var builder: Retrofit.Builder

    init {
        val cache = Cache(File(Utils.app.externalCacheDir, "ok-cache"), 1024 * 1024 * 30L)

        // 创建OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(HttpLoggingInterceptor { message -> LogUtil.e(TAG, message) }.setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(CacheInterceptor())
                .addNetworkInterceptor(CacheNetworkInterceptor())
                .cache(cache)
                .build()

        // 创建Retrofit实例
        builder = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

        if (!TextUtils.isEmpty(API.BASE_URL)) {
            retrofit = builder.baseUrl(API.BASE_URL).build()
        }
    }

    class CacheNetworkInterceptor : Interceptor {
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

    class CacheInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response
            val request: Request
            if (NetworkUtil.isConnected()) {
                //有网络，检查10秒内的缓存
                request = chain.request()
                    .newBuilder()
                    .cacheControl(
                        CacheControl.Builder()
                            .maxAge(10, TimeUnit.SECONDS)
                            .build()
                    )
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

    private object Holder {
        val retrofitFactory = RetrofitFactory()
    }

    companion object {
        private const val TAG = "RetrofitFactory"

        private const val TIME_OUT: Long = 10L

        val instance: RetrofitFactory = Holder.retrofitFactory

    }
}
