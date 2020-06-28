package com.nfragiskatos.fraggram.activities.main.fragments.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    private val _navigateToSignInFragment = MutableLiveData<Boolean>()
    val navigateToSignInFragment: LiveData<Boolean>
        get() = _navigateToSignInFragment

    fun displaySignInFragment() {
        _navigateToSignInFragment.value = true
    }

    fun displaySignInFragmentComplete() {
        _navigateToSignInFragment.value = false
    }
}