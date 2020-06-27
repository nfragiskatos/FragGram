package com.nfragiskatos.fraggram.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _navigateToEditProfileActivity = MutableLiveData<Boolean>()
    val navigateToEditProfileActivity: LiveData<Boolean>
        get() = _navigateToEditProfileActivity

    fun displayEditProfileActivity() {
        _navigateToEditProfileActivity.value = true
    }

    fun displayEditProfileActivityComplete() {
        _navigateToEditProfileActivity.value = false
    }
}