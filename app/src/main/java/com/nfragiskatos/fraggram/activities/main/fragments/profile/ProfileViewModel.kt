package com.nfragiskatos.fraggram.activities.main.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.User

class ProfileViewModel : ViewModel() {

    private val _numFollowers = MutableLiveData<Long>()
    val numFollowers: LiveData<Long>
        get() = _numFollowers

    private val _numFollowing = MutableLiveData<Long>()
    val numFollowing: LiveData<Long>
        get() = _numFollowing

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String>
        get() = _fullName

    private val _bio = MutableLiveData<String>()
    val bio: LiveData<String>
        get() = _bio

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private val _profilePhotoUri = MutableLiveData<String>()
    val profilePhotoUri: LiveData<String>
        get() = _profilePhotoUri

    private val _navigateToEditProfileActivity = MutableLiveData<Boolean>()
    val navigateToEditProfileActivity: LiveData<Boolean>
        get() = _navigateToEditProfileActivity

    fun displayEditProfileActivity() {
        _navigateToEditProfileActivity.value = true
    }

    fun displayEditProfileActivityComplete() {
        _navigateToEditProfileActivity.value = false
    }

    fun initProfileInfo(profileUid: String) {
        setUserInfoData(profileUid)
        setFollowersData(profileUid)
        setFollowingData(profileUid)
    }

    private fun setUserInfoData(profileUid: String) {
        Firebase.database.getReference("users/$profileUid/")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        p0.getValue(User::class.java)?.let { user ->
                            _username.value = user.username_display
                            _fullName.value = user.fullName_display
                            _bio.value = user.bio
                            _profilePhotoUri.value = user.profileImageUrl
                        }
                    }
                }
            })
    }

    private fun setFollowersData(profileUid: String) {
        Firebase.database.getReference("follow/${profileUid}/followers/")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        _numFollowers.value = p0.childrenCount
                    }
                }
            })
    }

    private fun setFollowingData(profileUid: String) {
        Firebase.database.getReference("follow/${profileUid}/following/")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        _numFollowing.value = p0.childrenCount
                    }
                }
            })
    }
}