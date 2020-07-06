package com.nfragiskatos.fraggram.activities.main.fragments.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nfragiskatos.fraggram.R
import com.nfragiskatos.fraggram.activities.main.domain.User
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
            SearchListAdapter(
                SearchListAdapter.SearchClickListener(this::onUserItemClick),
                SearchListAdapter.SearchClickListener(this::onFollowClick),
                SearchListAdapter.SearchClickListener(this::onUnFollowClick)
            )

        binding.edittextSearchTermSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchForUsers(s.toString().toLowerCase())
            }
        })

        viewModel.navigateToUserProfileFragment.observe(viewLifecycleOwner, Observer { user ->
            if (user != null && context != null) {


                val prefs = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
                prefs?.putString("profileId", user.uid)
                prefs?.apply()

                // Navigating via the bottom navigation view
                val bnv = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                bnv?.selectedItemId = R.id.navigation_profile

                // An alternate method to navigate to the new fragment. Doesn't seem as clean, because
                // we are bypassing the navigation component, and when the navigation is complete, the
                // Search icon in the bottom navigation is still selected.
                // Not sure what the benefits are to doing it this way.
//                val fragmentActivity = context as FragmentActivity
//                val beginTransaction = fragmentActivity.supportFragmentManager.beginTransaction()
//                beginTransaction.replace(R.id.fragment_container_test, ProfileFragment()).commit()
//                viewModel.displayUserProfileFragmentCompleted()
//                Log.d(TAG, "")
            }
        })

        return binding.root
    }

    private fun onUserItemClick(user: User) {
        Log.d(TAG, "Navigating to ${user.fullName_display}'s profile")
        viewModel.displayUserProfileFragment(user)
    }

    private fun onFollowClick(user: User) {
        Log.d(
            TAG,
            "Following username: ${user.username_display}\nfull name: ${user.fullName_display}"
        )
        viewModel.followUser(user)
    }

    private fun onUnFollowClick(user: User) {
        Log.d(
            TAG,
            "Unfollowing username: ${user.username_display}\nfull name: ${user.fullName_display}"
        )
        viewModel.unFollowUser(user)
    }
}