package com.nfragiskatos.fraggram.activities.main.fragments.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {
    private val _navigateToSignUpFragment = MutableLiveData<Boolean>()
    val navigateToSignUpFragment: LiveData<Boolean>
        get() = _navigateToSignUpFragment

    fun displaySignUpFragment() {
        _navigateToSignUpFragment.value = true
    }

    fun displaySignUpFragmentComplete() {
        _navigateToSignUpFragment.value = false
    }
}