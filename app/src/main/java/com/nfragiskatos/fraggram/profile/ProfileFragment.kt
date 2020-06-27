package com.nfragiskatos.fraggram.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nfragiskatos.fraggram.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToEditProfileActivity.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                Log.d(TAG, "CLICKED EDIT PROFILE")
            }
        })

        return binding.root
    }

}