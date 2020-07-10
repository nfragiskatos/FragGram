package com.nfragiskatos.fraggram.activities.main.fragments.addpost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nfragiskatos.fraggram.databinding.FragmentAddPostBinding
import com.theartofdev.edmodo.cropper.CropImage

class AddPostFragment : Fragment() {

    private val TAG = "AddPostFragment"

    companion object {
        fun newInstance() = AddPostFragment()
    }

    private val viewModel: AddPostViewModel by lazy {
        ViewModelProvider(this).get(AddPostViewModel::class.java)
    }

    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddPostBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.imageviewImagePostAddPost.setOnClickListener {
            context?.let {
                CropImage.activity().setAspectRatio(2, 1)
                    .start(it, this)
            }
        }

        viewModel.notification.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT)
                .show()
        })

        viewModel.logMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it)
        })

        viewModel.navigateToHomeFragment.observe(viewLifecycleOwner, Observer {navigate ->
            if (navigate) {
                findNavController().popBackStack()
                viewModel.displayHomeFragmentCompleted()
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                AddPostStatus.LOADING -> {
                    binding.framelayoutProgressbarHolderAddPost.visibility = View.VISIBLE
                    setBackgroundEnabled(false)
                }

                else -> {
                    binding.framelayoutProgressbarHolderAddPost.visibility = View.INVISIBLE
                    setBackgroundEnabled(true)
                }
            }
        })


        return binding.root
    }

    private fun setBackgroundEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.updateProfileImageUri(CropImage.getActivityResult(data).uri.toString())
        }
    }
}