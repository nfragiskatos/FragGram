package com.nfragiskatos.fraggram.activities.main.fragments.accountsettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.User

class AccountSettingsViewModel : ViewModel() {

    init {
        setUserInfoData()
    }

    val fullName = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val bio = MutableLiveData<String>()
    val photoUri = MutableLiveData<String>()
    private lateinit var currentUser: User

    private val _navigateToSignInFragment = MutableLiveData<Boolean>()
    val navigateToSignInFragment: MutableLiveData<Boolean>
        get() = _navigateToSignInFragment

    private val _navigateToProfileFragment = MutableLiveData<Boolean>()
    val navigateToProfileFragment: LiveData<Boolean>
        get() = _navigateToProfileFragment

    fun displaySignInFragment() {
        _navigateToSignInFragment.value = true
    }

    fun displaySignInFragmentComplete() {
        _navigateToSignInFragment.value = false
    }

    fun displayProfileFragment() {
        _navigateToProfileFragment.value = true
    }

    fun displayProfileFragmentCompleted() {
        _navigateToProfileFragment.value = false
    }

    fun signOutUser() {
        Firebase.auth.signOut()
        displaySignInFragment()
    }

    private fun setUserInfoData() {
        val profileUid = Firebase.auth.uid ?: return
        Firebase.database.getReference("users/$profileUid/")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        p0.getValue(User::class.java)?.let { user ->
                            username.value = user.username_display
                            fullName.value = user.fullName_display
                            bio.value = user.bio
                            photoUri.value = user.profileImageUrl
                            currentUser = user
                        }
                    }
                }
            })
    }

    fun saveUserInfoData() {
        val fullName = this.fullName.value
        val username = this.username.value
        val bio = this.bio.value

        if (fullName == null || username == null || bio == null) {
//            _notification.value = "Please enter text for all fields."
            return
        }

        if (fullName == null) return

        val uid = Firebase.auth.uid ?: return
        val reference = Firebase.database.getReference("/users/$uid")

        val userMap = HashMap<String, Any>()
        userMap["fullName_display"] = fullName
        userMap["fullName_sort"] = fullName.toLowerCase()
        userMap["username_display"] = username
        userMap["username_sort"] = username.toLowerCase()
        userMap["bio"] = bio

        reference.updateChildren(userMap)
        displayProfileFragment()
    }
}