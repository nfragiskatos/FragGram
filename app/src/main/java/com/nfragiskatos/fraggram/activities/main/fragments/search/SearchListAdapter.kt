package com.nfragiskatos.fraggram.activities.main.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.databinding.ListViewUserItemBinding
import com.squareup.picasso.Picasso

class SearchListAdapter(private val onFollowClickListener: OnFollowClickListener) :
    ListAdapter<User, SearchListAdapter.SearchItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        return SearchItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onFollowClickListener)
    }


    class SearchItemViewHolder private constructor(private val binding: ListViewUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, onFollowClickListener: OnFollowClickListener) {
            binding.textviewUsernameUserItem.text = user.username
            binding.textviewFullNameUserItem.text = user.fullName
            binding.buttonFollowUserItem.setOnClickListener {
                onFollowClickListener.onClick(user)
            }
            if (user.profileImageUrl != null && user.profileImageUrl.isNotEmpty()) {
                Picasso.get().load(user.profileImageUrl)
                    .into(binding.circleimageviewProfileUserItem)
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

    class OnFollowClickListener(val clickListener: (user: User) -> Unit) {
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