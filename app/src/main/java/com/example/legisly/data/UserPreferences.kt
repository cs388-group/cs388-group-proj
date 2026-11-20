
package com.example.legisly.data

import android.content.Context

class UserPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(
        "legisly_preferences",
        Context.MODE_PRIVATE
    )

    fun saveInterests(interests: List<String>) {
        preferences.edit()
            .putStringSet("selected_interests", interests.toSet())
            .apply()
    }

    fun getInterests(): Set<String> {
        return preferences.getStringSet(
            "selected_interests",
            emptySet()
        )?.toSet() ?: emptySet()
    }

    fun saveZipCode(zipCode: String) {
        preferences.edit()
            .putString("zip_code", zipCode)
            .apply()
    }

    fun getZipCode(): String {
        return preferences.getString("zip_code", "") ?: ""
    }

    fun saveRegionName(regionName: String) {
        preferences.edit()
            .putString("region_name", regionName)
            .apply()
    }

    fun getRegionName(): String {
        return preferences.getString("region_name", "") ?: ""
    }
}
