
package com.example.legisly.models

/**
 * Represents a legislative topic a LEG ISLY user can follow.
 *
 * id is the stable key used to save preferences.
 * title is the name displayed in the app.
 * description briefly explains the topic.
 */
data class Interest(
    val id: String,
    val title: String,
    val description: String,
    var isSelected: Boolean = false
)
