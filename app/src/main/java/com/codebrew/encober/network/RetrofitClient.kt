package com.codebrew.encober.network

import com.codebrew.encober.utils.ApiConstants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var encoberApi: EncoberApi

    init {
        val retrofit = initRetrofitClient()
        encoberApi = retrofit.create(EncoberApi::class.java)
    }

    fun getApi(): EncoberApi = encoberApi


    private fun initRetrofitClient(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_PATH)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }


}
