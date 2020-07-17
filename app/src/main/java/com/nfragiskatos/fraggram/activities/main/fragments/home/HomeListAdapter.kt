package com.nfragiskatos.fraggram.activities.main.fragments.home

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.activities.main.domain.Post
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.databinding.ListViewPostItemBinding
import com.squareup.picasso.Picasso

class HomeListAdapter : ListAdapter<Post, HomeListAdapter.HomeItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        return HomeItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }


    class HomeItemViewHolder private constructor(private val binding: ListViewPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.textviewDescriptionPostItem.text = post.description

            val ref = Firebase.database.getReference("users/${post.publisherId}")
            ref.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (!p0.exists()) {
                        return
                    }

                    val user = p0.getValue(User::class.java)

                    user?.let {
                        binding.textviewUsernamePostItem.text = it.username_display
                        Picasso.get().load(it.profileImageUrl).into(binding.circleimageviewProfilePostItem)
                        binding.textviewPublisherPostItem.text = user.fullName_display
                    }
                }

            })


            Picasso.get().load(post.imageUrl).into(binding.imageviewPostImagePostItem)
        }

        companion object {
            fun from(parent: ViewGroup): HomeItemViewHolder {
                val binding = ListViewPostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HomeItemViewHolder(binding)
            }
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }


}