package com.example.formtreeapp.retrofit

import com.example.formtreeapp.models.Form
import com.example.formtreeapp.models.GetFormsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FormTreeApiService {
    @GET("form")
    fun getForms(@Header("Authorization") authorization: String): Call<GetFormsResponse>

    @GET("form/{id}")
    fun getForm(@Header("Authorization") authorization: String, @Path("id") id: String): Call<Form>
}