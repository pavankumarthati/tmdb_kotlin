package com.careem.tmdb.careemapp.daggers

import com.careem.tmdb.careemapp.common.API_KEY
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain) : Response {
        val request = chain.request();
        var url = request.url();
        url = url.newBuilder()
                .addQueryParameter("api_key", API_KEY).build();
        val requestBuilder : Request.Builder= request.newBuilder();
        requestBuilder.url(url);
        val newRequest : Request = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}