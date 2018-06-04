package com.careem.tmdb.careemapp.daggers

import android.content.Context
import com.careem.tmdb.careemapp.common.BASE_URL
import com.careem.tmdb.careemapp.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class NetworkModule {



    @Provides
    @Singleton
    fun provideCache(context: Context) : Cache {
        val cacheSize : Long = 5 * 1024 * 1024; // 5 MB
        val cacheDir : File = context.getCacheDir();
        return Cache(cacheDir, cacheSize);
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        if (BuildConfig.DEBUG) {
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    @Provides
    @Singleton
    fun provideApiInterceptor() : Interceptor = ApiInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor , apiInterceptor: Interceptor) : OkHttpClient {
        return OkHttpClient.Builder().apply {
            this.addNetworkInterceptor(apiInterceptor)
            this.addInterceptor(httpLoggingInterceptor)
            this.cache(cache)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRxjavaAdapterFactory() : RxJava2CallAdapterFactory{
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) : Retrofit {
        return Retrofit.Builder().apply {
            this.addCallAdapterFactory(rxJava2CallAdapterFactory)
            this.baseUrl(BASE_URL)
            this.client(okHttpClient)
            this.addConverterFactory(MoshiConverterFactory.create())
        }.build()
    }

}