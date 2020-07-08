package com.nfragiskatos.fraggram.repositories

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    suspend fun followUser(userToFollowUid: String) {
        withContext(Dispatchers.IO) {
            val uid = Firebase.auth.uid
            uid?.let {
                val followingRef =
                    Firebase.database.getReference("follow/$uid/following/${userToFollowUid}")
                        .push()
                val followersRef =
                    Firebase.database.getReference("follow/${userToFollowUid}/followers/$uid")
                        .push()

                followersRef.setValue(true).await()
                followingRef.setValue(true).await()
            }
        }
    }

    suspend fun unFollowUser(userToFollowUid: String) {
        withContext(Dispatchers.IO) {
            val uid = Firebase.auth.uid
            uid?.let {
                val followingRef =
                    Firebase.database.getReference("follow/$uid/following/${userToFollowUid}")
                val followersRef =
                    Firebase.database.getReference("follow/${userToFollowUid}/followers/$uid")

                followersRef.setValue(null).await()
                followingRef.setValue(null).await()
            }
        }
    }

    private fun validatePath(nodePath: String): String {
        return if (nodePath[nodePath.lastIndex] == '/') nodePath else "$nodePath/"
    }

    suspend fun uploadImageToStorage(uri: String, fileName: String) : Uri? {
        return withContext(Dispatchers.IO) {
            val ref = Firebase.storage.getReference("/images/$fileName")
            ref.putFile(Uri.parse(uri)).await()
            return@withContext ref.downloadUrl.await()
        }
    }

    suspend fun updateUserInfo(fullName: String, username: String, bio: String, uri: String) {
        val uid = Firebase.auth.uid ?: return
        val reference = Firebase.database.getReference("/users/$uid")

        val userMap = HashMap<String, Any>()
        userMap["fullName_display"] = fullName
        userMap["fullName_sort"] = fullName.toLowerCase()
        userMap["username_display"] = username
        userMap["username_sort"] = username.toLowerCase()
        userMap["bio"] = bio
        userMap["profileImageUrl"] = uri

        reference.updateChildren(userMap).await()
    }
}