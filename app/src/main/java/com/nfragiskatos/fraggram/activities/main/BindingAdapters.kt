package com.nfragiskatos.fraggram.activities.main

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nfragiskatos.fraggram.R
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.activities.main.fragments.search.SearchListAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("userSearchData")
fun bindUserSearchRecyclerView(recyclerView: RecyclerView, data: List<User>?) {
    val adapter = recyclerView.adapter as SearchListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("profileUrl")
fun bindProfileUrl(profileImageView: ImageView, url: String?) {
    url?.let {
        Picasso.get().load(url).placeholder(R.drawable.ic_baseline_person_64)
            .into(profileImageView)
    }
}

@BindingAdapter("longData")
fun bindLongData(textView: TextView, data: Long?) {
    data?.let {
        textView.text = "$data"
    }
}