package com.emranul.weatherupdate.hilt

import android.content.Context
import com.emranul.weatherupdate.api.ApiInterface
import com.emranul.weatherupdate.api.adapter.FlowCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl = "https://api.openweathermap.org/".toHttpUrl()

    @Singleton
    @Provides
    fun provideMockServerInterceptor(@ApplicationContext context: Context): Interceptor {
        return MockServerInterceptor(context)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideOkHttpClient(mockServerInterceptor: Interceptor) =
        OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            //.addInterceptor(mockServerInterceptor)
            .build()


    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideRetrofit(mBaseUrl: HttpUrl, mClient: OkHttpClient, mMoshi: Moshi): Retrofit =
        Retrofit.Builder()
            .client(mClient)
            .baseUrl(mBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(mMoshi))
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideApiService(mRetrofit: Retrofit): ApiInterface =
        mRetrofit.create(ApiInterface::class.java)
}
