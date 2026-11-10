package com.example.legisly.models

/**
 * Represents one topic that a CivicPulse user can follow.
 *
 * id is a stable key for saving preferences.
 * title is the label displayed in the app.
 * description gives the user a little more context.
 */
data class Interest(
    val id: String,
    val title: String,
    val description: String,
    var isSelected: Boolean = false
)
