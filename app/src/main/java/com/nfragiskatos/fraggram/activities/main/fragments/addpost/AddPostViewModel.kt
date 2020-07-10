package com.nfragiskatos.fraggram.activities.main.fragments.addpost

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.repositories.FirebaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class AddPostStatus { LOADING, ERROR, DONE }

class AddPostViewModel : ViewModel() {

    val postDescription = MutableLiveData<String>()

    private val _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String>
        get() = _photoUri
    private var photoChanged: Boolean = false

    private val _navigateToHomeFragment = MutableLiveData<Boolean>()
    val navigateToHomeFragment: LiveData<Boolean>
        get() = _navigateToHomeFragment

    private val _notification = MutableLiveData<String>()
    val notification: LiveData<String>
        get() = _notification

    private val _logMessage = MutableLiveData<String>()
    val logMessage: LiveData<String>
        get() = _logMessage

    private val _status = MutableLiveData<AddPostStatus>()
    val status: LiveData<AddPostStatus>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun displayHomeFragment() {
        _navigateToHomeFragment.value = true
    }

    fun displayHomeFragmentCompleted() {
        _navigateToHomeFragment.value = false
    }

    fun updateProfileImageUri(uri: String) {
        _photoUri.value = uri
        photoChanged = true
    }

    fun uploadPost() {

        val imageUri = _photoUri.value
        val desc = postDescription.value

        when {
            imageUri == null -> {
                _notification.value = "Please select an image to upload."
                return
            }

            desc == null || TextUtils.isEmpty(desc) -> {
                _notification.value = "Please enter a description."
                return
            }

            else -> {

                _status.value = AddPostStatus.LOADING
                coroutineScope.launch {
                    val ext = imageUri.substring(imageUri.lastIndexOf(".") + 1)
                    val fileName =
                        "${Firebase.auth.uid}_${System.currentTimeMillis().toString()}.$ext"
                    val nodePath = "/Post Pictures/"

                    val retUri =
                        FirebaseRepository.uploadImageToStorage(imageUri, fileName, nodePath)

                    retUri?.let {
                        FirebaseRepository.saveNewPost(desc, it.toString())
                    }
                    _status.value = AddPostStatus.DONE
                    _notification.value = "Post uploaded successfully"
                    displayHomeFragment()
                }
            }
        }
    }
}