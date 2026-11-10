package com.example.legisly.data

import android.content.Context

/**
 * Small local persistence layer for onboarding.
 *
 * SharedPreferences is appropriate for these simple settings.
 * Do not store passwords or API secrets here.
 */
class UserPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun saveZipCode(zipCode: String) {
        preferences.edit().putString(KEY_ZIP_CODE, zipCode).apply()
    }

    fun getZipCode(): String {
        return preferences.getString(KEY_ZIP_CODE, "") ?: ""
    }

    fun saveRegionName(regionName: String) {
        preferences.edit().putString(KEY_REGION_NAME, regionName).apply()
    }

    fun getRegionName(): String {
        return preferences.getString(KEY_REGION_NAME, "") ?: ""
    }

    fun saveInterests(interestIds: Set<String>) {
        preferences.edit().putStringSet(KEY_INTERESTS, interestIds.toSet()).apply()
    }

    fun getInterests(): Set<String> {
        return preferences.getStringSet(KEY_INTERESTS, emptySet())?.toSet() ?: emptySet()
    }

    companion object {
        private const val PREFS_NAME = "legisly_preferences"
        private const val KEY_ZIP_CODE = "zip_code"
        private const val KEY_REGION_NAME = "region_name"
        private const val KEY_INTERESTS = "interests"
    }
}
