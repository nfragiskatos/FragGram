package com.nfragiskatos.fraggram.activities.main.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nfragiskatos.fraggram.activities.main.domain.User

class SearchViewModel : ViewModel() {
    private val _users = MutableLiveData<MutableList<User>>(mutableListOf())
    val users: LiveData<MutableList<User>>
        get() = _users

    // TODO: DELETE
    fun initMockData() {
        var data = mutableListOf<User>()
        data.add(User("", "Harry Potter", "hpotter", "", "", ""))
        data.add(User("", "Ron Weasley", "rweasley", "", "", ""))
        data.add(User("", "Hermione Granger", "hgranger", "", "", ""))
        data.add(User("", "Draco Malfoy", "dmalfoy", "", "", ""))
        data.add(User("", "Rubeus Hagrid", "rhagrid", "", "", ""))
        _users.value = data
    }
}