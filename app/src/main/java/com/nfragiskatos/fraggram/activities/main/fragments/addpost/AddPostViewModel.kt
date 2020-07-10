package com.nfragiskatos.fraggram.activities.main.fragments.addpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddPostViewModel : ViewModel() {
    private val _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String>
        get() = _photoUri
    private var photoChanged: Boolean = false

    fun updateProfileImageUri(uri: String) {
        _photoUri.value = uri
        photoChanged = true
    }
}