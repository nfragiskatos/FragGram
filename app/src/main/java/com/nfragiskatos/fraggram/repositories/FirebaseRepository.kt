package com.nfragiskatos.fraggram.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseRepository {

    suspend fun performSignUp(email: String, password: String) : AuthResult? {
        return withContext(Dispatchers.IO) {
            return@withContext Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        }
    }
}