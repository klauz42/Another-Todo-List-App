package ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments.viewcontroller

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.itemtouchhelper.TodoItemListTouchHelperCallback
import ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.recyclerview.TodoItemListAdapter
import ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments.TodoItemListFragment
import ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments.TodoItemListFragmentDirections
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class TodoItemListViewController @Inject constructor(
    private var fragment: TodoItemListFragment,
    private val itemTouchHelperCallback: TodoItemListTouchHelperCallback
) : TodoItemListAdapter.Listener {

    private val appBarLayoutBehavior: AppBarLayout.Behavior by lazy {
        getAppbarLayoutParams()
    }

    private val binding get() = fragment.binding
    private val viewModel get() = fragment.viewModel
    private val recyclerViewAdapter get() = fragment.recyclerViewAdapter

    private fun AppCompatActivity.setAppBarTitleVisibility(isVisible: Boolean) =
        this.supportActionBar?.setDisplayShowTitleEnabled(isVisible)

    fun initialize() {
        setupAppBar()
        setupRecyclerView()
        setupItemsObserver()
        setupUndoneCountObserver()
        setupShowHideDoneButton()
        setupAddItemButton(::navigateToDetails)
    }

    private fun navigateToDetails(id: UUID, isNewItem: Boolean) {
        val idString = id.toString()
        val action =
            TodoItemListFragmentDirections.actionListToDetails(idString, isNewItem)
        fragment.findNavController().navigate(action)
    }

    private fun getAppbarLayoutParams(): AppBarLayout.Behavior {
        val layoutParams =
            binding.appBarLayout.layoutParams
                    as CoordinatorLayout.LayoutParams
        if (layoutParams.behavior == null)
            layoutParams.behavior = AppBarLayout.Behavior()

        return layoutParams.behavior as AppBarLayout.Behavior
    }

    private fun setupAppBar() {
        binding.toolbar.setupWithNavController(fragment.findNavController())

        (fragment.requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            setAppBarTitleVisibility(false)
        }

        binding.appBarLayout.addOnOffsetChangedListener(fragment)
    }

    private fun submitRecyclerViewAdapterList(itemList: List<TodoItemDomainEntity>) {

        (binding.recyclerView.adapter as TodoItemListAdapter).submitList(itemList)

        binding.loadingIndicator.visibility = View.GONE

        if (itemList.isEmpty()) {
            binding.apply {
                noItemsMessage.visibility = View.VISIBLE
                appBarLayout.setExpanded(false)
                recyclerView.isNestedScrollingEnabled = false
            }
            appBarLayoutBehavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
        } else {
            binding.apply {
                noItemsMessage.visibility = View.GONE
                recyclerView.isNestedScrollingEnabled = true
            }
            appBarLayoutBehavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return true
                }
            })
        }
    }

    private fun setupShowHideDoneButton() {
        val listener = View.OnClickListener { viewModel.toggleShowDone() }
        binding.expandedHeaderView.showHideDoneButton.setOnClickListener(listener)
        binding.collapsedHeaderView.showHideDoneButton.setOnClickListener(listener)

        viewModel.showDone.observe(fragment.viewLifecycleOwner) { showDone ->
            val icon: Drawable? = if (showDone) {
                ContextCompat.getDrawable(fragment.requireContext(), R.drawable.visibility_off)
            } else {
                ContextCompat.getDrawable(fragment.requireContext(), R.drawable.visibility)
            }
            icon?.let {
                binding.expandedHeaderView.showHideDoneButton.setImageDrawable(it)
                binding.collapsedHeaderView.showHideDoneButton.setImageDrawable(it)
            }
        }
    }

    private fun setupRecyclerView() {
        val itemAnimator = object : DefaultItemAnimator() {
            //override to prevent shimmering animation when outermost items deleted
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder) = true
        }

        recyclerViewAdapter.setAdapterListener(this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = recyclerViewAdapter
            this.itemAnimator = itemAnimator
        }

        val helperCallback = itemTouchHelperCallback.apply {
            setAdapterListener(recyclerViewAdapter)
        }

        ItemTouchHelper(helperCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun setUndoneCount(count: Int) {
        binding.expandedHeaderView.headerSubtitle.text =
            fragment.requireContext().getString(R.string.undone_count_format, count)
    }

    private fun cleanUndoneCount() {
        binding.expandedHeaderView.headerSubtitle.text = ""
    }

    private fun setupUndoneCountObserver() {
        viewModel.countDoneLiveData.observe(fragment.viewLifecycleOwner) { count ->
            if (count != null) setUndoneCount(count)
            else cleanUndoneCount()
        }
    }

    private fun setupAddItemButton(
        navigateToDetailFragment: (id: UUID, isNewItem: Boolean) -> Unit
    ) {
        binding.addItemFab.setOnClickListener {
            val newEntity = TodoItemDomainEntity()
            viewModel.addTodoItem(newEntity)
            navigateToDetailFragment(newEntity.id, true)
        }
    }

    private fun setupItemsObserver() {
        viewModel.todoItems.observe(fragment.viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Success -> {
                    submitRecyclerViewAdapterList(result.data)
                }

                is DataResult.Error -> displayError(result.error)
                is DataResult.Loading -> displayLoading()
                else -> {}
            }
        }
    }

    private fun displayError(exception: Throwable) {
        val snackbar = Snackbar.make(
            binding.itemListCoordinatorLayout,
            fragment.requireContext().getString(
                R.string.error_occurred_format,
                exception.toString()
            ),
            Snackbar.LENGTH_SHORT,
        )
        snackbar.show()
    }

    private fun displayLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
    }

    override fun itemClickListener(id: UUID) {
        navigateToDetails(id, false)
    }

    override fun doneCheckBoxListener(item: TodoItemDomainEntity, isDone: Boolean) {
        viewModel.updateTodoItemDoneStatus(item, isDone)
    }

    override fun moveItemListener(fromId: UUID, toId: UUID) {
        viewModel.moveTodoItemsInList(fromId, toId)
    }

    override fun deleteItemListener(item: TodoItemDomainEntity) {
        viewModel.deleteTodoItem(item)
    }

    override fun undoItemDeletionListener(onDeletionCallback: () -> Unit) {
        val snackbar = Snackbar.make(
            binding.itemListCoordinatorLayout,
            R.string.undo_deletion_snackbar,
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction(R.string.restore_todo) {
            viewModel.undoTodoItemDeletion()
            onDeletionCallback()
        }
        snackbar.show()
    }
}