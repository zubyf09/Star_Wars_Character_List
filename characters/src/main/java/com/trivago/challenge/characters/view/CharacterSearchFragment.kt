package com.trivago.challenge.characters.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trivago.challenge.characters.R
import com.trivago.challenge.characters.adapter.CharacterSearchAdapter
import com.trivago.challenge.characters.viewmodel.CharacterSearchVM
import com.trivago.challenge.view.BaseFragment
import com.trivago.challenge.view.extensions.EndlessScrollListener
import com.trivago.challenge.view.extensions.gone
import com.trivago.challenge.view.extensions.visible
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.actionbar_toolbar.*
import kotlinx.android.synthetic.main.fragment_search_character.*
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


class CharacterSearchFragment : BaseFragment(),
    CharacterSearchAdapter.Interaction {

    override val layout = R.layout.fragment_search_character

    override val viewModel: CharacterSearchVM by inject()

    private val adapter: CharacterSearchAdapter by lazy {
        CharacterSearchAdapter(
            this
        )
    }

    //Search
    private var searchView: SearchView? = null
    private val searchListener = PublishSubject.create<String>()

    //Pagination
    private lateinit var endlessScrollListener: EndlessScrollListener

    //Navigator
    private var navigator: CharacterNavigator? = null

    companion object {
        const val TAG = "CharacterSearchFragment"
        fun newInstance() = CharacterSearchFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CharacterNavigator)
            navigator = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(toolbar)
        parentActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }

        rvCharacters.setHasFixedSize(true)
        rvCharacters.adapter = adapter

        srlCharacters.setOnRefreshListener {
            refreshCharacters()
        }

        btnShowAllCharacters.setOnClickListener {
            refreshCharacters()
        }

        startListeningToPaginationLoadingState()

        startListeningToCharacters()

        initSearchBehaviour()

        viewModel.initialLoad()

        endlessScrollListener = initEndlessScroll()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val myActionMenuItem = menu.findItem(R.id.item_search)
        searchView = myActionMenuItem.actionView as SearchView
        searchView?.queryHint = getString(R.string.enter_character_name)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchListener.onNext(newText)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        rvCharacters.addOnScrollListener(endlessScrollListener)
    }

    private fun refreshCharacters() {
        llNoData.gone()
        searchView?.setQuery("", false)
        searchView?.clearFocus()
        viewModel.refreshCharacters()
    }

    private fun startListeningToPaginationLoadingState() {
        viewModel.paginationLoading.observe(this, Observer {
            if (it) {
                pbLoading.visible()
            } else pbLoading.gone()
        })
    }

    private fun initSearchBehaviour() {
        //A listener to get user's query and manipulate it before going to vm
        searchListener
                //To ensure queries are run when the user pauses typing
                .debounce(800, TimeUnit.MILLISECONDS)
                .subscribe {
                    //Reset the pagination state
                    endlessScrollListener.resetState()
                    viewModel.searchCharacter(it)
                }
                .addTo(viewModel.disposable)
    }

    private fun startListeningToCharacters() {
        viewModel.characters.observe(this, Observer { characters ->
            if (characters.isEmpty()) {
                llNoData?.visible()
                rvCharacters?.gone()
            } else {
                adapter.swapData(characters)

                // Data swap takes little bit time, so if we show before data swap is completed
                // then it can show old data for short time. So to avoid that added little delay.
                Timer().schedule(1000){
                    parentActivity.runOnUiThread {
                        llNoData?.gone()
                        rvCharacters?.visible()
                    }
                }
            }
        })
    }

    private fun initEndlessScroll() = object : EndlessScrollListener(
            layoutManager = rvCharacters.layoutManager as LinearLayoutManager,
            visibleThreshold = 2) {
        //This will be called each time the user scrolls
        // and only 2 elements are left in the recyclerview items.
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.loadNextPage()
        }
    }

    override fun hideLoading() {
        srlCharacters.isRefreshing = false
    }

    override fun showLoading() {
        srlCharacters.isRefreshing = true
    }

    interface CharacterNavigator {
        fun showCharacterDetails(character: com.trivago.challenge.characters.model.CharacterSearchModel)
    }

    //Adapter interactions
    override fun characterClicked(character: com.trivago.challenge.characters.model.CharacterSearchModel) {
        searchView?.clearFocus()
        navigator?.showCharacterDetails(character)
    }

}
