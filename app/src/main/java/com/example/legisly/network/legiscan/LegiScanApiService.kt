package com.example.legisly.network.legiscan

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface LegiScanApiService {

    @GET("./")
    suspend fun getSessionList(
        @Query("key") apiKey: String,
        @Query("op") op: String = "getSessionList",
        @Query("state") state: String
    ): JsonObject

    @GET("./")
    suspend fun getMasterList(
        @Query("key") apiKey: String,
        @Query("op") op: String = "getMasterList",
        @Query("id") sessionId: Int
    ): JsonObject
}
