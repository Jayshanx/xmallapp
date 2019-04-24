package com.me.guanpj.mall.library.data.net

import android.util.Log
import com.kotlin.base.common.BaseConstant
import com.me.guanpj.mall.library.logging.Level
import com.me.guanpj.mall.library.logging.LoggingInterceptor
import com.me.guanpj.mall.library.logging.MyLogginInterceptor
import com.me.guanpj.mall.library.util.AppPrefsUtils
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

class RetrofitFactory private constructor() {

    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val interceptor: Interceptor
    private val retrofit: Retrofit

    private val logger = HttpLoggingInterceptor.Logger { message ->
        try {
            val text = URLDecoder.decode(message, "utf-8")
            Log.e("OKHttp-----", text)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            Log.e("OKHttp-----", message)
        }
    }

    init {

        interceptor = Interceptor { chain ->
            val request = chain.request()
                    .newBuilder()
                    .addHeader("Content_Type", "application/json")
                    .addHeader("charset", "UTF-8")
                    .addHeader("token", AppPrefsUtils.instant.getString(BaseConstant.KEY_SP_TOKEN))
                    .build()

            chain.proceed(request)
        }

        //Retrofit实例化
        retrofit = Retrofit.Builder()
                .baseUrl(BaseConstant.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(initClient())
                .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(initLogInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
    }


    private fun initClent2(): OkHttpClient {


        return OkHttpClient.Builder()
                .addInterceptor(MyLogginInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
    }


    private fun initOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.Builder()//构建者模式
                        .loggable(true) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("Content_Type", "application/json")
                        .addHeader("charset", "UTF-8")
                        .build()
                )
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build()
    }

    private fun loggingInterceptor(): LoggingInterceptor {

        val interceptor = LoggingInterceptor.Builder().loggable(true) //是否开启日志打印
                .setLevel(Level.BASIC) //打印的等级
                .log(Platform.INFO) // 打印类型
                .request("Request") // request的Tag
                .response("Response")// Response的Tag
                .addHeader("Content_Type", "application/json")
                .addHeader("charset", "UTF-8")
        return interceptor.build()
    }

    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(logger)
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}
