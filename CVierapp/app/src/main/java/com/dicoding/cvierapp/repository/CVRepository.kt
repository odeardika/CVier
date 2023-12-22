package com.dicoding.cvierapp.repository

import android.util.Log
import com.dicoding.cvierapp.response.CVResponse
import com.dicoding.cvierapp.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CVRepository {
    suspend fun postCV(file: MultipartBody.Part, job: RequestBody): Result<CVResponse> {
        return suspendCoroutine { continuation ->
            val client = ApiConfig.getApiService().postCV(file, job)
            client.enqueue(object : Callback<CVResponse> {
                override fun onResponse(call: Call<CVResponse>, response: Response<CVResponse>) {
                    if (response.isSuccessful) {
                        continuation.resume(Result.success(response.body() as CVResponse))
                    } else {
                        Log.e("CVRepository", "Unsuccessful response. Status code: ${response.code()}")
                        continuation.resume(
                            Result.failure(
                                Throwable(response.errorBody()?.string() ?: "Unknown error")
                            )
                        )
                    }
                }
                override fun onFailure(call: Call<CVResponse>, t: Throwable) {
                    continuation.resume(Result.failure(t))
                }
            })
        }
    }
}