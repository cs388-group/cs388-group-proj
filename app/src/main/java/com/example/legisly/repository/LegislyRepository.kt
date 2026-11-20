
package com.example.legisly.repository

import com.example.legisly.network.ApiClient
import com.google.gson.JsonObject

class LegislyRepository(
    private val apiKey: String
) {

    // STEP 1: Find federal representatives using ZIP code
    suspend fun getRepresentatives(zipCode: String): JsonObject {

        require(zipCode.matches(Regex("\\d{5}"))) {
            "Enter a valid 5-digit ZIP code"
        }

        return ApiClient.congressApi.getRepresentatives(zipCode)
    }

    // STEP 2: Retrieve legislative sessions for the state
    suspend fun getSessions(state: String): JsonObject {

        return ApiClient.legiscanApi.getSessionList(
            apiKey = apiKey,
            state = state
        )
    }

    // STEP 3: Retrieve bills for the selected session
    suspend fun getBills(sessionId: Int): JsonObject {

        return ApiClient.legiscanApi.getMasterList(
            apiKey = apiKey,
            sessionId = sessionId
        )
    }

    // STEP 4: Retrieve additional information about a bill
    suspend fun getBillDetails(billId: Int): JsonObject {

        return ApiClient.legiscanApi.getBill(
            apiKey = apiKey,
            billId = billId
        )
    }

    // STEP 5: Retrieve state legislators
    suspend fun getStateLegislators(
        sessionId: Int
    ): JsonObject {

        return ApiClient.legiscanApi.getSessionPeople(
            apiKey = apiKey,
            sessionId = sessionId
        )
    }
}
