package com.accedo.codetest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.accedo.codetest.databinding.HolderCharacterBinding
import com.accedo.codetest.data.network.Character


class CharacterPagedListAdapter : PagedListAdapter<Character, CharacterPagedListAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }


    class CharacterViewHolder(private val binding: HolderCharacterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(character: Character){
            binding.character = character
        }

        companion object {
            fun from(viewGroup: ViewGroup): CharacterViewHolder {
                return CharacterViewHolder(HolderCharacterBinding.inflate(LayoutInflater.from(viewGroup.context)))
            }
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }

    }
}