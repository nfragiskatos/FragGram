package com.nfragiskatos.fraggram.activities.main.fragments.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nfragiskatos.fraggram.repositories.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class SignInStatus { LOADING, ERROR, DONE }

class SignInViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

    private val _navigateToHomeFragment = MutableLiveData<Boolean>()
    val navigateToHomeFragment: LiveData<Boolean>
        get() = _navigateToHomeFragment

    private val _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    private val _logMessage = MutableLiveData<String>()
    val logMessage: LiveData<String>
        get() = _logMessage

    private val _status = MutableLiveData<SignInStatus>()
    val status: LiveData<SignInStatus>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun displaySignUpFragment() {
        _navigateToSignUpFragment.value = true
    }

    fun displaySignUpFragmentComplete() {
        _navigateToSignUpFragment.value = false
    }

    fun displayHomeFragment() {
        _navigateToHomeFragment.value = true
    }

    fun displayHomeFragmentComplete() {
        _navigateToHomeFragment.value = false
    }

    fun signIn() {
        val email = this.email.value
        val password = this.password.value

        if (email == null || password == null) {
            _notification.value = "Please enter email and password."
            return
        }

        coroutineScope.launch {
            _status.value = SignInStatus.LOADING
            val result = FirebaseRepository.performLogIn(email, password)

            if (result != null) {
                _logMessage.value = "${result.user?.email} successfully logged in"
                displayHomeFragment()
                _status.value = SignInStatus.DONE
            } else {
                _notification.value = "Failed to log in user"
                _logMessage.value = "Failed to log in user"
            }
        }

    }
}