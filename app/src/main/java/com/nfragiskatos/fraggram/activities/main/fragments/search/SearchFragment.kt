package com.nfragiskatos.fraggram.activities.main.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nfragiskatos.fraggram.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val TAG = "SearchFragment"

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerViewSearch.adapter =
            SearchListAdapter(SearchListAdapter.SearchClickListener { user ->
                Log.d(TAG, "Following username: ${user.username_display}\nfull name: ${user.fullName_display}")
                viewModel.followUser(user)
            }, SearchListAdapter.SearchClickListener { user ->
                Log.d(TAG, "Unfollowing username: ${user.username_display}\nfull name: ${user.fullName_display}")
                viewModel.unFollowUser(user)
            })

        binding.edittextSearchTermSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchForUsers(s.toString().toLowerCase())
            }
        })

        return binding.root
    }
}