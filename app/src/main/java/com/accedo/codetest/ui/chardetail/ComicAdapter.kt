package com.accedo.codetest.ui.chardetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.accedo.codetest.data.network.Comic
import com.accedo.codetest.databinding.HolderComicBinding

class ComicAdapter(private val listener : OnClickComic) : ListAdapter<Comic,ComicAdapter.ComicViewHolder>(ComicDiffCallback()){


    interface OnClickComic {
        fun onClick (comic: Comic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }


    class ComicViewHolder(private val binding : HolderComicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: Comic, listener: OnClickComic) {
            binding.comic = comic
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(viewGroup: ViewGroup): ComicViewHolder {
                return ComicViewHolder(HolderComicBinding.inflate(LayoutInflater.from(viewGroup.context)))
            }
        }
    }


    class ComicDiffCallback : DiffUtil.ItemCallback<Comic>() {
        override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
            return oldItem == newItem
        }

    }
}