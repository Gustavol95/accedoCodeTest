package com.accedo.codetest.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.accedo.codetest.R
import com.accedo.codetest.ui.home.CharacterPagedListAdapter
import com.accedo.codetest.data.network.Character
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: PagedList<Character>?) {
    val adapter = recyclerView.adapter as CharacterPagedListAdapter
    adapter.submitList(data)
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .dontTransform()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("description")
fun bindDescription(textView: TextView, description: String?) {
    textView.text = when(description.isNullOrEmpty()) {
        true -> "No description"
        else -> description
    }
}
