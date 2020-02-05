package com.accedo.codetest.ui.chardetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.accedo.codetest.R
import com.accedo.codetest.data.network.Comic
import com.accedo.codetest.data.network.Network
import com.accedo.codetest.data.network.Status
import com.accedo.codetest.data.repository.CharacterRepository
import com.accedo.codetest.databinding.FragmentCharDetailBinding
import com.accedo.codetest.utils.getSimpleMessage
import timber.log.Timber

class CharacterDetailFragment : Fragment() {

    val characterRepository = CharacterRepository(Network.marvelService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition= ChangeBounds().apply {
            duration = 750
        }
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
        binding.recycler.setHasFixedSize(true)

        viewmodel.comicLiveList(args.idCharacter).observe(viewLifecycleOwner,  Observer {
            when (it) {
                is Status.Success<*> ->{
                    Timber.i("Success: ${it.response.data.results}")
                    val adapter = binding.recycler.adapter as ComicAdapter
                    val list = it.response.data.results as List<Comic>
                    adapter.submitList(list)
                    binding.frameLoading.visibility = View.GONE
                }
                is Status.Failure -> {
                    Timber.i("Failure: ${it.throwable}")
                    Toast.makeText(requireContext(),it.throwable.getSimpleMessage(), Toast.LENGTH_LONG).show()
                    binding.frameLoading.visibility = View.GONE

                }
                is Status.Loading -> {
                    Timber.i("Loading")
                    binding.frameLoading.visibility = View.VISIBLE
                }
            }
        })

        return binding.root
    }
}