package com.nfragiskatos.fraggram.repositories

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseRepository {

    suspend fun performLogIn(email: String, password: String): AuthResult? {
        return withContext(Dispatchers.IO) {
            return@withContext Firebase.auth.signInWithEmailAndPassword(email, password).await()
        }
    }

    suspend fun performSignUp(email: String, password: String): AuthResult? {
        return withContext(Dispatchers.IO) {
            return@withContext Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        }
    }

    suspend fun saveUserInfo(user: User, nodePath: String) {
        withContext(Dispatchers.IO) {
            val path = validatePath(nodePath)
            val ref = Firebase.database.getReference(path + user.uid)
            ref.setValue(user).await()
        }
    }

    suspend fun followUser(userToFollow: User) {
        withContext(Dispatchers.IO) {
            val uid = Firebase.auth.uid
            uid?.let {
                val followingRef =
                    Firebase.database.getReference("follow/$uid/following/${userToFollow.uid}")
                        .push()
                val followersRef =
                    Firebase.database.getReference("follow/${userToFollow.uid}/followers/$uid")
                        .push()

                followersRef.setValue(true).await()
                followingRef.setValue(true).await()
            }
        }
    }

    suspend fun unFollowUser(userToFollow: User) {
        withContext(Dispatchers.IO) {
            val uid = Firebase.auth.uid
            uid?.let {
                val followingRef =
                    Firebase.database.getReference("follow/$uid/following/${userToFollow.uid}")
                val followersRef =
                    Firebase.database.getReference("follow/${userToFollow.uid}/followers/$uid")

                followersRef.setValue(null).await()
                followingRef.setValue(null).await()
            }
        }
    }

    private fun validatePath(nodePath: String): String {
        return if (nodePath[nodePath.lastIndex] == '/') nodePath else "$nodePath/"
    }
}