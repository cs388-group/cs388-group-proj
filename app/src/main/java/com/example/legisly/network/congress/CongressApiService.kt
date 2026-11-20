

package com.example.legisly.network.congress

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface CongressApiService {

    @GET("getall_mems.php")
    suspend fun getRepresentatives(
        @Query("zip") zipCode: String,
        @Query("output") output: String = "json"
    ): JsonObject
}
