package ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.viewcontroller

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.searchtodos.adapters.SearchResultListAdapter
import ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.SearchTodosFragment
import ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.SearchTodosFragmentDirections
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class SearchTodosViewController @Inject constructor(
    private val fragment: SearchTodosFragment
) : SearchResultListAdapter.ItemClickListener {

    private val binding get() = fragment.binding
    private val viewModel get() = fragment.viewModel
    private val recyclerViewAdapter get() = fragment.recyclerViewAdapter

    fun initialize() {
        setupRecyclerView()
        setupSortOptionsButtonListener()
        setupFilterOptionsButtonListener()
        setupChangeRecyclerViewLayoutManagerButtonListener()
        setupRecyclerViewLayoutManagerObserver()
        setupSearchTextEditObserver()
        setupItemsObserver()
    }

    private fun submitRecyclerViewAdapterList(itemList: List<TodoItemDomainEntity>) {
        (binding.recyclerView.adapter as SearchResultListAdapter).submitList(itemList)
    }

    private fun setupItemsObserver() {
        viewModel.filteredAndSortedSearchResult.observe(fragment.viewLifecycleOwner) { items ->
            submitRecyclerViewAdapterList(items)
        }
    }

    private fun setupSortOptionsButtonListener() {
        binding.buttonSortOptions.setOnClickListener {
            navigateToSortOptions()
        }
    }

    private fun setupFilterOptionsButtonListener() {
        binding.buttonFilterOptions.setOnClickListener {
            navigateToFilterOptions()
        }

    }

    private fun setupChangeRecyclerViewLayoutManagerButtonListener() {
        binding.buttonChangeRecyclerviewLayoutManager.setOnClickListener {
            viewModel.toggleLayoutManager()
        }
    }

    private fun setupRecyclerViewLayoutManagerObserver() {
        viewModel.layoutViewType.observe(fragment.viewLifecycleOwner) { layoutManagerType ->
            setRecyclerViewLayoutManager(layoutManagerType)
        }
    }

    private fun setupSearchTextEditObserver() {
        binding.editTextSearch.addTextChangedListener {
            viewModel.setSearchingText(it.toString())
        }
    }

    private fun setRecyclerViewLayoutManager(managerType: SearchLayoutViewType) {
        val recyclerView = binding.recyclerView
        val context = recyclerView.context

        val drawable: Drawable

        recyclerView.layoutManager = when (managerType) {
            SearchLayoutViewType.LINEAR -> {
                recyclerViewAdapter.layoutManagerType = SearchLayoutViewType.LINEAR
                drawable = ContextCompat.getDrawable(context, R.drawable.grid_view)!!
                LinearLayoutManager(context)
            }

            SearchLayoutViewType.GRID -> {
                recyclerViewAdapter.layoutManagerType = SearchLayoutViewType.GRID
                drawable = ContextCompat.getDrawable(context, R.drawable.list_view)!!
                val spanCount =
                    fragment.resources.getInteger(R.integer.item_search_layout_grid_span)
                StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
            }
        }

        binding.buttonChangeRecyclerviewLayoutManager.setImageDrawable(drawable)
    }

    private fun setupRecyclerView() {
        recyclerViewAdapter.setAdapterListener(this)

        val manager = viewModel.layoutViewType.value ?: SearchLayoutViewType.LINEAR
        setRecyclerViewLayoutManager(manager)
        binding.recyclerView.adapter = recyclerViewAdapter
    }

    private fun navigateToDetails(id: UUID) {
        val idString = id.toString()
        val action =
            SearchTodosFragmentDirections.actionListToDetails(idString, false)
        fragment.findNavController().navigate(action)
    }

    private fun navigateToSortOptions() {
        val action = SearchTodosFragmentDirections.actionSortOptions()
        fragment.findNavController().navigate(action)
    }

    private fun navigateToFilterOptions() {
        val action = SearchTodosFragmentDirections.actionFilterOptions()
        fragment.findNavController().navigate(action)
    }

    override fun onItemClickListener(id: UUID) {
        navigateToDetails(id)
    }
}
