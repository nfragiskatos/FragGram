package com.nfragiskatos.fraggram.activities.main.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
                val button = it as Button
                if (button.text.toString() == "Follow") {
                    button.text = "Unfollow"
                    button.background = itemView.context.resources.getDrawable(R.drawable.button_background_follow)
//                    button.setBackgroundColor(itemView.context.resources.getColor(R.color.colorPrimary))
                    button.setTextColor(itemView.context.resources.getColor(android.R.color.white))
                    onFollowClickListener.onClick(user)
                } else {
                    button.text = "Follow"
                    button.background = itemView.context.resources.getDrawable(R.drawable.button_background)
                    button.setTextColor(itemView.context.resources.getColor(android.R.color.black))
                    onUnFollowClickListener.onClick(user)
                }
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