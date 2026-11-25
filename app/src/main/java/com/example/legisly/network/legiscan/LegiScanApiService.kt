
package com.example.legisly.network.legiscan

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface LegiScanApiService {

    // Retrieve legislative sessions for a state
    @GET("/")
    suspend fun getSessionList(
        @Query("key") apiKey: String,
        @Query("op") operation: String = "getSessionList",
        @Query("state") state: String
    ): JsonObject

    // Retrieve all bills in a legislative session
    @GET("/")
    suspend fun getMasterList(
        @Query("key") apiKey: String,
        @Query("op") operation: String = "getMasterList",
        @Query("id") sessionId: Int
    ): JsonObject

    // Retrieve an individual bill
    @GET("/")
    suspend fun getBill(
        @Query("key") apiKey: String,
        @Query("op") operation: String = "getBill",
        @Query("id") billId: Int
    ): JsonObject
    @GET("/")
    suspend fun getSessionPeople(
        @Query("key") apiKey: String,
        @Query("op") operation: String = "getSessionPeople",
        @Query("id") sessionId: Int
    ): JsonObject
}
