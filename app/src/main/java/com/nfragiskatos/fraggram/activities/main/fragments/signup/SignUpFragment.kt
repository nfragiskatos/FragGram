package com.nfragiskatos.fraggram.activities.main.fragments.signup

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nfragiskatos.fraggram.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private val TAG = "RegisterFragment"
    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToSignInFragment.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                this.findNavController().navigateUp()
                viewModel.displaySignInFragmentComplete()
            }
        })

        viewModel.notification.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT)
                .show()
        })

        viewModel.logMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it)
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {status ->


            when (status) {
                SignUpStatus.LOADING -> {
                    binding.progressBarHolder.visibility = View.VISIBLE
                    setBackgroundEnabled(false)
                }
                else -> {
                    binding.progressBarHolder.visibility = View.INVISIBLE
                    setBackgroundEnabled(true)
                }
            }
        })

        return binding.root
    }

    private fun setBackgroundEnabled(isEnabled: Boolean) {
        binding.edittextFullNameSignUp.isEnabled = isEnabled
        binding.edittextUsernameSignUp.isEnabled = isEnabled
        binding.edittextEmailSignUp.isEnabled = isEnabled
        binding.edittextPasswordSignUp.isEnabled = isEnabled
        binding.buttonSignUpSignUp.isEnabled = isEnabled
        binding.buttonSignInSignUp.isEnabled = isEnabled
    }
}