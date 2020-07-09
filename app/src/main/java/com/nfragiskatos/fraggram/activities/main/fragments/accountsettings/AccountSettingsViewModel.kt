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
import com.nfragiskatos.fraggram.repositories.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AccountSettingsViewModel : ViewModel() {

    init {
        setUserInfoData()
    }

    val fullName = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val bio = MutableLiveData<String>()
    private val _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String>
        get() = _photoUri
    private var photoChanged: Boolean = false

    private lateinit var currentUser: User

    private val _navigateToSignInFragment = MutableLiveData<Boolean>()
    val navigateToSignInFragment: MutableLiveData<Boolean>
        get() = _navigateToSignInFragment

    private val _navigateToProfileFragment = MutableLiveData<Boolean>()
    val navigateToProfileFragment: LiveData<Boolean>
        get() = _navigateToProfileFragment

    private val _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    private val _logMessage = MutableLiveData<String>()
    val logMessage: LiveData<String>
        get() = _logMessage

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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

    fun updateProfileImageUri(uri: String) {
        _photoUri.value = uri
        photoChanged = true
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
                            _photoUri.value = user.profileImageUrl
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
        val imageUri = this._photoUri.value

        if (fullName == null || username == null || bio == null) {
            _notification.value = "Please enter text for all fields."
            return
        }

        coroutineScope.launch {
            if (photoChanged && imageUri != null) {

                val ext = imageUri.substring(imageUri.lastIndexOf(".") + 1)
                val fileName = "${Firebase.auth.uid}_profile_image.$ext"

                val retUri = FirebaseRepository.uploadImageToStorage(imageUri, fileName)
                retUri?.let {
                    FirebaseRepository.updateUserInfo(fullName, username, bio, it.toString())
                }
            } else {
                FirebaseRepository.updateUserInfo(fullName, username, bio, imageUri!!)
            }
            displayProfileFragment()
        }
    }
}