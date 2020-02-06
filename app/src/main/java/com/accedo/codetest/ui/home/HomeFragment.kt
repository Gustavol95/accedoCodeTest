package com.accedo.codetest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.transition.TransitionInflater
import com.accedo.codetest.R
import com.accedo.codetest.data.network.Character
import com.accedo.codetest.data.network.Network
import com.accedo.codetest.data.network.Status
import com.accedo.codetest.data.repository.CharacterRepository
import com.accedo.codetest.databinding.FragmentHomeBinding
import com.accedo.codetest.utils.getSimpleMessage
import com.accedo.codetest.utils.makeRounded
import com.accedo.codetest.utils.waitForTransition
import com.google.android.material.snackbar.Snackbar
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
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        binding.recycler.apply {
            this.adapter = CharacterPagedListAdapter(
                object : CharacterPagedListAdapter.OnClickCharacter {
                    override fun onClick(character: Character, extras: FragmentNavigator.Extras) {
                        view!!.findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToCharacterDetailFragment(
                                character.id,
                                character.description,
                                character.thumbnail.getUrl(),
                                character.name
                            ),extras)
                    }
                }
            )
        }

        binding.lifecycleOwner = this

        binding.viewmodel = viewModel

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshCharacterList()
        }

        binding.executePendingBindings()
        waitForTransition(binding.recycler)

        viewModel.networkStatus.observe(viewLifecycleOwner, Observer {
            Timber.i("Stauts: $it ")
            when (it) {
                is Status.Success<*> -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                is Status.Failure -> {
                    binding.swipeRefresh.isRefreshing = false
                    Snackbar.make(binding.coordinator, it.throwable.getSimpleMessage(), Snackbar.LENGTH_INDEFINITE)
                        .makeRounded().show()
                }
                is Status.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
            }
        })



        return binding.root
    }

}