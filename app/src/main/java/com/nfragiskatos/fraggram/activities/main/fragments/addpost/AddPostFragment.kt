package com.nfragiskatos.fraggram.activities.main.fragments.addpost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.updateProfileImageUri(CropImage.getActivityResult(data).uri.toString())
        }
    }
}