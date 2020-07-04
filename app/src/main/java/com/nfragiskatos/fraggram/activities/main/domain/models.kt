package com.nfragiskatos.fraggram.activities.main.domain


/**
 * Domain model to represent a user in the app
 */
data class User(
    val uid: String,
    val fullName_display: String,
    val fullName_sort: String,
    val username_display: String,
    val username_sort: String,
    val email: String,
    val bio: String,
    val profileImageUrl: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}