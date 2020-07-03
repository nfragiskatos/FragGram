package com.nfragiskatos.fraggram.activities.main.fragments.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.repositories.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class SignUpStatus { LOADING, ERROR, DONE }

class SignUpViewModel : ViewModel() {

    val fullName = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _navigateToSignInFragment = MutableLiveData<Boolean>()
    val navigateToSignInFragment: LiveData<Boolean>
        get() = _navigateToSignInFragment

    private val _navigateToHomeFragment = MutableLiveData<Boolean>()
    val navigateToHomeFragment: LiveData<Boolean>
    get() = _navigateToHomeFragment

    private val _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    private val _logMessage = MutableLiveData<String>()
    val logMessage: LiveData<String>
        get() = _logMessage

    private val _status = MutableLiveData<SignUpStatus>()
    val status: LiveData<SignUpStatus>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun displaySignInFragment() {
        _navigateToSignInFragment.value = true
    }

    fun displaySignInFragmentComplete() {
        _navigateToSignInFragment.value = false
    }

    fun displayHomeFragment() {
        _navigateToHomeFragment.value = true
    }

    fun displayHomeFragmentComplete() {
        _navigateToHomeFragment.value = false
    }

    fun performSignUp() {
        val fullName = this.fullName.value
        val username = this.username.value
        val email = this.email.value
        val password = this.password.value

        if (fullName == null || username == null || email == null || password == null) {
            _notification.value = "Please enter text for all fields."
            return
        }

        coroutineScope.launch {
            _status.value = SignUpStatus.LOADING
            val result = FirebaseRepository.performSignUp(email, password)
            if (result != null) {
                _logMessage.value =
                    "Successfully creates user with\nuid: ${result.user?.uid}\nemail: ${result.user?.email}"

                val uid = Firebase.auth.uid ?: ""
                val user = User(
                    uid,
                    fullName,
                    username,
                    email,
                    "Hey this is a default bio.",
                    "https://firebasestorage.googleapis.com/v0/b/fraggram-9d41c.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=be3b8d46-2bdb-48e5-8382-41c3f71995a9"
                )
                FirebaseRepository.saveUserInfo(user, "/users/")
                _status.value = SignUpStatus.DONE
                displayHomeFragment()
            } else {
                Firebase.auth.signOut()
            }
        }
    }
}