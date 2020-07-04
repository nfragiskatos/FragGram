package com.nfragiskatos.fraggram.activities.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nfragiskatos.fraggram.activities.main.domain.User
import com.nfragiskatos.fraggram.activities.main.fragments.search.SearchListAdapter

@BindingAdapter("userSearchData")
fun bindUserSearchRecyclerView(recyclerView: RecyclerView, data: List<User>?) {
    val adapter = recyclerView.adapter as SearchListAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}