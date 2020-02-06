package com.accedo.codetest.ui.chardetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.accedo.codetest.R
import com.accedo.codetest.data.network.Comic
import com.accedo.codetest.data.network.Network
import com.accedo.codetest.data.network.Status
import com.accedo.codetest.data.repository.CharacterRepository
import com.accedo.codetest.databinding.FragmentCharDetailBinding
import com.accedo.codetest.utils.getSimpleMessage
import com.accedo.codetest.utils.makeRounded
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class CharacterDetailFragment : Fragment() {

    private val characterRepository = CharacterRepository(Network.marvelService)

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.no_transition).apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentCharDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_char_detail, container, false)
        val args = CharacterDetailFragmentArgs.fromBundle(arguments!!)
        val viewModel = ViewModelProvider(this, CharacterViewModelFactory(args.idCharacter, characterRepository))
            .get(CharacterDetailViewModel::class.java)
        swipeRefresh = binding.swipeRefresh!!
        binding.args = args
        binding.lifecycleOwner = this
        binding.recycler.adapter = ComicAdapter(object: ComicAdapter.OnClickComic {
            override fun onClick(comic: Comic) {
                Timber.i("$comic")
            }
        })

        binding.recycler.setHasFixedSize(true)

        viewModel.comicList.observe(viewLifecycleOwner,  Observer {
            when (it) {
                is Status.Success<*> ->{
                    Timber.i("Success: ${it.response.data.results}")
                    val adapter = binding.recycler.adapter as ComicAdapter
                    @Suppress("UNCHECKED_CAST")
                    val list = it.response.data.results as List<Comic>
                    adapter.submitList(list)
                    swipeRefresh.isRefreshing = false
                }
                is Status.Failure -> {
                    Timber.i("Failure: ${it.throwable}")
                    snackBar = Snackbar.make(binding.coordinator, it.throwable.getSimpleMessage(), Snackbar.LENGTH_INDEFINITE)
                        .makeRounded()
                        .setAction(getString(R.string.retry)) { viewModel.refresh(args.idCharacter) }

                    snackBar?.show()
                    swipeRefresh.isRefreshing = false
                }
                is Status.Loading -> {
                    Timber.i("Loading")
                    snackBar?.dismiss()
                    swipeRefresh.isRefreshing = true
                }
            }
        })

        viewModel.emptyListLiveData.observe(viewLifecycleOwner, Observer {
            Timber.i("LIST IS EMPTY LIVEDATA: $it")
            binding.recycler.visibility = if(it)  View.GONE else View.VISIBLE
            binding.tvEmpty.visibility = if(it)  View.VISIBLE else View.GONE
        })



        swipeRefresh.setOnRefreshListener {
            viewModel.refresh(args.idCharacter)
        }
        (activity as AppCompatActivity).supportActionBar?.title = args.name

        Glide.with(requireContext())
            .load(args.urlThumbnail)
            .dontTransform()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .listener(object: RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })
            .into(binding.ivThumbnail)


        binding.executePendingBindings()

        return binding.root
    }
}