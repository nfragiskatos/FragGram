package com.nfragiskatos.fraggram.activities.main.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel : ViewModel() {

    init {
        checkFollowings()
    }

    private val _posts = MutableLiveData<MutableList<Post>>(mutableListOf())
    val posts: LiveData<MutableList<Post>>
        get() = _posts

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private fun checkFollowings() {
        val followingList = mutableListOf<String>()
        val uid = Firebase.auth.uid
        uid?.let {
            val followingRef =
                Firebase.database.getReference("follow/$uid/following/")

            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (snapshot in p0.children) {
                            snapshot.key?.let {
                                followingList.add(it)
                            }
                        }

                        retrievePosts(followingList)
                    }
                }

            })
        }
    }

    private fun retrievePosts(users: List<String>) {
        val ref = Firebase.database.getReference("posts")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                _posts.value = mutableListOf()

                for (snapshot in p0.children) {
                    val post = snapshot.getValue(Post::class.java)

                    post?.let {
                        for (id in users) {
                            if (post?.publisherId == id) {
                                _posts.value!!.add(it)
                            }
                        }
                    }
                }
            }

        })
    }
}