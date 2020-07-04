package com.nfragiskatos.fraggram.activities.main.fragments.search

import android.os.Bundle
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
            SearchListAdapter(SearchListAdapter.OnFollowClickListener {
                Log.d(TAG, "username: ${it.username}\nfull name: ${it.fullName}")
            })

        viewModel.initMockData()

        return binding.root
    }
}