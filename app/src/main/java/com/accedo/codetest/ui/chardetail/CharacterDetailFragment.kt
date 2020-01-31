package com.accedo.codetest.ui.chardetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.accedo.codetest.R
import com.accedo.codetest.data.network.Comic
import com.accedo.codetest.data.network.Network
import com.accedo.codetest.data.network.Status
import com.accedo.codetest.data.repository.CharacterRepository
import com.accedo.codetest.databinding.FragmentCharDetailBinding
import timber.log.Timber

class CharacterDetailFragment : Fragment() {

    val characterRepository = CharacterRepository(Network.marvelService)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCharDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_char_detail, container, false)
        val args = CharacterDetailFragmentArgs.fromBundle(arguments!!)
        val viewmodel = ViewModelProvider(this, CharacterViewModelFactory(characterRepository))
            .get(CharacterDetailViewModel::class.java)
        binding.args = args
        binding.lifecycleOwner = this
        binding.recycler.adapter = ComicAdapter(object: ComicAdapter.OnClickComic {
            override fun onClick(comic: Comic) {
                Timber.i("$comic")
            }
        })

        viewmodel.comicLiveList(args.idCharacter).observe(viewLifecycleOwner,  Observer {
            when (it) {
                is Status.Success<*> ->{
                    Timber.i("Success: ${it.response.data.results}")
                    val adapter = binding.recycler.adapter as ComicAdapter
                    val list = it.response.data.results as List<Comic>
                    adapter.submitList(list)
                }
                is Status.Failure -> {
                    Timber.i("Failure: ${it.throwable}")
                }
                is Status.Loading -> {
                    Timber.i("Loading")
                }
            }
        })
        (activity as AppCompatActivity).supportActionBar?.title = args.name

        return binding.root
    }
}