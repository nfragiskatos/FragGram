package com.nfragiskatos.fraggram.activities.main.fragments.accountsettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountSettingsViewModel : ViewModel() {

    private val _navigateToSingInFragment = MutableLiveData<Boolean>()
    val navigateToSingInFragment: MutableLiveData<Boolean>
        get() = _navigateToSingInFragment

    fun displaySignInFragment() {
        _navigateToSingInFragment.value = true;
    }

    fun displaySignInFragmentComplete() {
        _navigateToSingInFragment.value = false;
    }

    fun signOutUser() {
        Firebase.auth.signOut()
        displaySignInFragment()
    }
}