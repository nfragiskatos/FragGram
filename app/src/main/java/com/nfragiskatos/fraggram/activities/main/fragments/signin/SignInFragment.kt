package com.nfragiskatos.fraggram.activities.main.fragments.signin

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nfragiskatos.fraggram.R
import com.nfragiskatos.fraggram.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by lazy {
        ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignInBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToSignUpFragment.observe(viewLifecycleOwner, Observer {navigate ->
            if (navigate) {
                this.findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
                viewModel.displaySignUpFragmentComplete()
            }
        })

        return binding.root
    }
}