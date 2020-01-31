package com.accedo.codetest.ui.chardetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.accedo.codetest.databinding.FragmentCharDetailBinding

class CharacterDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharDetailBinding.inflate(inflater)
        val args = CharacterDetailFragmentArgs.fromBundle(arguments!!)
        binding.args = args
        return binding.root
    }
}