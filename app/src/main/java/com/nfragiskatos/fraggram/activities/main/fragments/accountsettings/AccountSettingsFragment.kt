package com.nfragiskatos.fraggram.activities.main.fragments.accountsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nfragiskatos.fraggram.databinding.FragmentAccountSettingsBinding

class AccountSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = AccountSettingsFragment()
    }

    private val viewModel: AccountSettingsViewModel by lazy {
        ViewModelProvider(this).get(AccountSettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAccountSettingsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToSingInFragment.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToSignInFragment())
                viewModel.displaySignInFragmentComplete()
            }
        })


        return binding.root
    }
}