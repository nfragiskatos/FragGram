package com.nfragiskatos.fraggram.activities.main.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.repositories.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    val searchTerm = MutableLiveData<String>()

    private val _users = MutableLiveData<MutableList<User>>(mutableListOf())
    val users: LiveData<MutableList<User>>
        get() = _users

    private var viewModelJjob = Job()
    private val coroutineScope = CoroutineScope(viewModelJjob + Dispatchers.Main)

    fun searchForUsers(term: String) {

        val query = Firebase.database.getReference("/users/").orderByChild("fullName_sort")
            .startAt(term.toLowerCase()).endAt(term.toLowerCase() + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _users.value?.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        _users.value?.add(user)
                    }
                }
                _users.value = _users.value
            }
        })
    }

    fun followUser(userToFollow: User) {
        coroutineScope.launch {
            FirebaseRepository.followUser(userToFollow)
        }
    }

    fun unFollowUser(userToUnFollow: User) {

    }
}