package com.accedo.codetest.utils

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.accedo.codetest.ui.home.CharacterPagedListAdapter
import com.accedo.codetest.data.network.Character

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: PagedList<Character>?) {
    val adapter = recyclerView.adapter as CharacterPagedListAdapter
    adapter.submitList(data)
}
