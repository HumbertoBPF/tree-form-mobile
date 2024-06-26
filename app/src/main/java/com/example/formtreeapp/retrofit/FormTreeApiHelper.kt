package com.example.formtreeapp.retrofit

import com.example.formtreeapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FormTreeApiHelper {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .build()

    val service: FormTreeApiService = retrofit.create(FormTreeApiService::class.java)
}