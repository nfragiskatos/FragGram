package com.nfragiskatos.fraggram.activities.main.domain


/**
 * Domain model to represent a user in the app
 */
data class User(
    val uid: String,
    val fullName: String,
    val username: String,
    val email: String,
    val bio: String,
    val profileImageUrl: String
) {

}