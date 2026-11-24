package com.example.legisly.network

import android.util.Log
import com.example.legisly.network.congress.CongressApiService
import com.example.legisly.network.legiscan.LegiScanApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Legisly/1.0 (Android)")
                    .header("Accept", "application/json, text/plain, */*")
                    .build()

                Log.d("LEGISCAN_DEBUG", "Request URL: ${request.url}")
                val response = chain.proceed(request)
                Log.d("LEGISCAN_DEBUG", "Response Code: ${response.code} Content-Type: ${response.header("Content-Type")}")
                response
            }
            .build()
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val congressApi: CongressApiService =
        createRetrofit(
            "https://whoismyrepresentative.com/"
        ).create(CongressApiService::class.java)

    val legiscanApi: LegiScanApiService =
        createRetrofit(
            "https://api.legiscan.com/"
        ).create(LegiScanApiService::class.java)
}
