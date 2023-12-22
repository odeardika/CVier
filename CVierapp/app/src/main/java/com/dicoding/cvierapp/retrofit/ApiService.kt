package com.dicoding.cvierapp.retrofit

import com.dicoding.cvierapp.response.CVResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("cv-predict")
    fun postCV(
        @Part file: MultipartBody.Part,
        @Part ("job") job: RequestBody,
    ): Call<CVResponse>

    @GET("cv-predict")
    suspend fun getResult(): Response<CVResponse>
}