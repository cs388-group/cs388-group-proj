package com.example.legisly.network

import com.example.legisly.network.congress.CongressApiService
import com.example.legisly.network.legiscan.LegiScanApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
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
