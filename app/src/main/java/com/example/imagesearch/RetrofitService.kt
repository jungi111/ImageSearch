package com.example.imagesearch

import com.image.model.data.SearchImageResponse
import com.image.utils.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    @Headers(HEADER + APP_KEY)
    @GET(SEARCH_IMAGE)
    fun getSearchImage(
            @Query(QUERY) searchText: String,
            @Query(SORT) sort: String = "accuracy",
            @Query(PAGE) page: Int,
            @Query(SIZE) size: Int = 30
    ): Call<SearchImageResponse>
}