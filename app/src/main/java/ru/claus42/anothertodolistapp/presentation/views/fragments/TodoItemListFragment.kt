package ru.claus42.anothertodolistapp.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemListBinding
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.adapters.TodoItemAdapter
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemListViewModel
import kotlin.math.abs

class TodoItemListFragment : Fragment(),  AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentTodoItemListBinding? = null
    private val binding get() = _binding!!

    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView!!


    private val viewModel: TodoItemListViewModel by viewModels {
        activity.appComponent.viewModelsFactory()
    }

    private var isHideToolbarView: Boolean = false

    private val activity: AppCompatActivity by lazy { requireActivity() as AppCompatActivity }

    private fun AppCompatActivity.setAppBarTitleVisibility(isVisible: Boolean) =
                    this.supportActionBar?.setDisplayShowTitleEnabled(isVisible)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _recyclerView = binding.recyclerView
        setupAppBar()

        viewModel.todoItems.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Success -> {

                    setupRecyclerView(result.data)
                }
                is DataResult.Error -> displayError(result.error)
                is DataResult.Loading -> displayLoading()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        _recyclerView = null

        super.onDestroyView()
    }

    //There is no need for menu at the moment
    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_todo_item_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.show_hide_done_action -> {
                        //TODO: implementation of show hide done button
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    fun displayError(exception: Throwable) {
        //todo: error displaying
    }

    fun displayLoading() {
        //todo: display loading animation
    }

    private fun setupAppBar() {
        binding.toolbar.setupWithNavController(findNavController())

        activity.run {
            setSupportActionBar(binding.toolbar)
            setAppBarTitleVisibility(false)
        }

        binding.appBarLayout.addOnOffsetChangedListener(this)

    }

    private fun setupRecyclerView(todoItems: List<TodoItemDomainEntity>) {
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = TodoItemAdapter()
        (recyclerView.adapter as TodoItemAdapter).submitList(todoItems)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()

        if (percentage == 1f && isHideToolbarView) {
            binding.collapsedHeaderView.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView
        } else if (percentage < 1f && !isHideToolbarView) {
            binding.collapsedHeaderView.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        }
    }
}
