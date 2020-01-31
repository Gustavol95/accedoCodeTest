package com.accedo.codetest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.network.Network
import com.accedo.codetest.data.repository.CharacterRepository
import com.accedo.codetest.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private val repository = CharacterRepository(Network.marvelService)
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory(repository))
            .get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.recycler.adapter = CharacterPagedListAdapter(
            object : CharacterPagedListAdapter.OnClickCharacter {
                override fun onClick(character: Character) {
                    Timber.i("$character")
                    view!!.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToCharacterDetailFragment(
                            character.id,
                            character.description,
                            character.thumbnail.getUrl()
                        )
                    )
                }
            }
        )
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }
}