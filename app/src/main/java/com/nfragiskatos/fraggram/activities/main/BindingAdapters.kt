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

@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        Picasso.get().load(url).placeholder(R.drawable.ic_baseline_person_64)
            .into(imageView)
    }
}

@BindingAdapter("longData")
fun bindLongData(textView: TextView, data: Long?) {
    data?.let {
        textView.text = "$data"
    }
}