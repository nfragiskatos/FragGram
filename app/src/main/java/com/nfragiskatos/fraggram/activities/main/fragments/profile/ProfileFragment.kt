package com.nfragiskatos.fraggram.activities.main.fragments.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.R
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var profileId: String = "none"
        var firebaseUser = Firebase.auth.currentUser!!

        val prefs = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)


        if (prefs != null) {
            profileId = prefs.getString("profileId", "none")!!
        }

        if (profileId == "none") {
            profileId = firebaseUser.uid
        }

        if (profileId == firebaseUser.uid) {
            binding.buttonEditProfileProfile.text = "Edit Profile"
        } else {
            binding.buttonEditProfileProfile.text = "Follow"
            setFollowAndFollowingBtnStatus(profileId)
        }

        setFollowersField(profileId)
        setFollowingField(profileId)
        setUserInfoFields(profileId)

        viewModel.navigateToEditProfileActivity.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                this.findNavController()
                    .navigate(ProfileFragmentDirections.actionNavigationProfileToAccountSettingsFragment())
                viewModel.displayEditProfileActivityComplete()
            }
        })

        return binding.root
    }

    private fun setUserInfoFields(profileId: String) {
        Firebase.database.getReference("users/$profileId/")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        val user = p0.getValue(User::class.java) ?: return
                        binding.textviewProfileProfile.text = user.username_display
                        binding.textviewFullNameProfile.text = user.fullName_display
                        binding.textviewBioProfile.text = user.bio
                        if (user.profileImageUrl != null && user.profileImageUrl.isNotEmpty()) {
                            Picasso.get().load(user.profileImageUrl)
                                .placeholder(R.drawable.ic_baseline_person_64)
                                .into(binding.circleImageViewProfileImageProfile)
                        }
                    }
                }

            })
    }

    private fun setFollowAndFollowingBtnStatus(profileId: String) {
        Firebase.auth.currentUser?.let { currentUser ->
            Firebase.database.getReference("follow/${currentUser.uid}/following/")
        }?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(profileId).exists()) {
                    binding.buttonEditProfileProfile.text = "Following"
                } else {
                    binding.buttonEditProfileProfile.text = "Follow"
                }
            }

        })
    }

    private fun setFollowersField(profileId: String) {
        Firebase.database.getReference("follow/${profileId}/followers/")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        binding.textviewTotalFollowersValueProfile.text = "${p0.childrenCount}"
                    }
                }

            })

    }

    private fun setFollowingField(profileId: String) {
        Firebase.database.getReference("follow/${profileId}/following/")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        binding.textviewTotalFollowingValueProfile.text = "${p0.childrenCount}"
                    }
                }
            })
    }

    override fun onStop() {
        super.onStop()

        context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()?.let {
            it.putString("profileId", Firebase.auth.uid)
            it.apply()
        }
    }

    override fun onPause() {
        super.onPause()

        context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()?.let {
            it.putString("profileId", Firebase.auth.uid)
            it.apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()?.let {
            it.putString("profileId", Firebase.auth.uid)
            it.apply()
        }
    }
}