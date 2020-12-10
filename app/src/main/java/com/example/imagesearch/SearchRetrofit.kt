package com.example.imagesearch

import android.util.Log
import com.image.utils.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchRetrofit {
    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val origin = chain.request()
            val request = origin.newBuilder()
                    .addHeader("Authorization", "KakaoAK 845b0939f305073bff9b240b762bd636")
                    .addHeader("Content-Type", "application/json")
                    .method(origin.method(),origin.body())
                    .build()
            return chain.proceed(request)
        }
    }

    private fun createOKHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        builder.addInterceptor(HeaderInterceptor())
        return builder.build()
    }

    private val retrofit =
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(createOKHttpClient())
                    .build()
}