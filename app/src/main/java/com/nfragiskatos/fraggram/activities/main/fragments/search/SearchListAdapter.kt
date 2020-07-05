package com.nfragiskatos.fraggram.activities.main.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.R
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.databinding.ListViewUserItemBinding
import com.squareup.picasso.Picasso

class SearchListAdapter(private val onFollowClickListener: SearchClickListener, private val onUnFollowClickListener: SearchClickListener) :
    ListAdapter<User, SearchListAdapter.SearchItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        return SearchItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onFollowClickListener, onUnFollowClickListener)
    }


    class SearchItemViewHolder private constructor(private val binding: ListViewUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, onFollowClickListener: SearchClickListener, onUnFollowClickListener: SearchClickListener) {
            binding.textviewUsernameUserItem.text = user.username_display
            binding.textviewFullNameUserItem.text = user.fullName_display
            binding.buttonFollowUserItem.setOnClickListener {
                val followBtn = it as Button
                if (followBtn.text.toString() == "Follow") {
                    onFollowClickListener.onClick(user)
                } else {
                    onUnFollowClickListener.onClick(user)
                }
            }

            checkIfUserFollowing(user.uid, binding.buttonFollowUserItem)


            if (user.profileImageUrl != null && user.profileImageUrl.isNotEmpty()) {
                Picasso.get().load(user.profileImageUrl)
                    .into(binding.circleimageviewProfileUserItem)
            }

        }

        private fun checkIfUserFollowing(userToFollowUid: String, followBtn: Button) {
            val uid = Firebase.auth.uid
            uid?.let {
                val followingRef =
                    Firebase.database.getReference("follow/$uid/following/")

                followingRef.addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child(userToFollowUid).exists()) {
                            setFollowBtn(true, followBtn)
                        } else {
                            setFollowBtn(false, followBtn)
                        }
                    }

                })

            }
        }

        private fun setFollowBtn(isFollowing: Boolean, followBtn: Button) {
            if (isFollowing) {
                followBtn.text = "Unfollow"
                followBtn.background = itemView.context.resources.getDrawable(R.drawable.button_background_follow)
                followBtn.setTextColor(itemView.context.resources.getColor(android.R.color.white))
            } else {
                followBtn.text = "Follow"
                followBtn.background = itemView.context.resources.getDrawable(R.drawable.button_background)
                followBtn.setTextColor(itemView.context.resources.getColor(android.R.color.black))
            }

        }

        companion object {
            fun from(parent: ViewGroup): SearchItemViewHolder {
                val binding = ListViewUserItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SearchItemViewHolder(binding)
            }
        }
    }

    class SearchClickListener(val clickListener: (user: User) -> Unit) {
        fun onClick(user: User) = clickListener(user)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}