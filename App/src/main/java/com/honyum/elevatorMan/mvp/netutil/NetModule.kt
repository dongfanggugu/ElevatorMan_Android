package com.honyum.elevatorMan.mvp.netutil

import com.honyum.elevatorMan.base.Config
import com.honyum.elevatorMan.net.base.NetConstant
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor



/**
 * Created by Star on 2017/11/24.
 */
/**
 * 网络模块依赖注入
 */
@Module
open class NetModule {
    @Provides
    @Singleton
    fun netClient(): Retrofit? {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ParamsInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
        return Retrofit.Builder()
                .baseUrl(Config.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }
}