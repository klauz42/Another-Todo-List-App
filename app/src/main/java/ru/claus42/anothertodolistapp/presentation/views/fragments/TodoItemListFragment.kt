package ru.claus42.anothertodolistapp.presentation.views.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.snackbar.Snackbar
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemListBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.adapters.TodoItemListAdapter
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemListViewModel
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity
import java.util.UUID
import javax.inject.Inject
import kotlin.math.abs

@FragmentScope
class TodoItemListFragment : Fragment(),
    AppBarLayout.OnOffsetChangedListener {
    private lateinit var listFragmentComponent: FragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TodoItemListViewModel by viewModels { viewModelFactory }

    private var _binding: FragmentTodoItemListBinding? = null
    private val binding get() = _binding!!

    private var appBarLayoutBehavior: AppBarLayout.Behavior? = null
    private var isHideToolbarView: Boolean = false

    private val activity: MainActivity by lazy { requireActivity() as MainActivity }
    private fun AppCompatActivity.setAppBarTitleVisibility(isVisible: Boolean) =
        this.supportActionBar?.setDisplayShowTitleEnabled(isVisible)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFragmentComponent =
            DaggerFragmentComponent.builder().activityComponent(activity.activityComponent).build()
        listFragmentComponent.inject(this)
    }

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

        val recyclerViewAdapter = initTodoItemAdapter()

        setupAppBar()

        setupRecyclerView(recyclerViewAdapter)
        setupAppbarLayoutParams()
        setupItemsObserver()
        setupUndoneCountObserver()
        setupShowHideDoneButton()
        setupAddItemButton()
    }

    override fun onDestroyView() {
        _binding = null
        appBarLayoutBehavior = null

        super.onDestroyView()
    }

    private fun displayError(exception: Throwable) {
        //todo: error displaying
    }

    private fun displayLoading() {
        //todo: display loading animation
    }

    private fun openDetailsFragment(id: UUID, isNewItem: Boolean) {
        val idString = id.toString()
        val action =
            TodoItemListFragmentDirections.actionListToDetails(idString, isNewItem)
        findNavController().navigate(action)
    }

    private fun setupAppBar() {
        binding.toolbar.setupWithNavController(findNavController())

        activity.run {
            setSupportActionBar(binding.toolbar)
            setAppBarTitleVisibility(false)
        }

        binding.appBarLayout.addOnOffsetChangedListener(this)
    }

    private fun setupAppbarLayoutParams() {
        val layoutParams =
            binding.appBarLayout.layoutParams
                    as CoordinatorLayout.LayoutParams
        if (layoutParams.behavior == null)
            layoutParams.behavior = AppBarLayout.Behavior()
        appBarLayoutBehavior = layoutParams.behavior as AppBarLayout.Behavior
    }

    private fun setupItemsObserver() {
        viewModel.todoItems.observe(viewLifecycleOwner) { result ->
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

    private fun setupAddItemButton() {
        binding.addItemFab.setOnClickListener {
            val newEntity = TodoItemDomainEntity()
            viewModel.addTodoItem(newEntity)
            openDetailsFragment(newEntity.id, true)
        }
    }

    private fun setUndoneCount(count: Int) {
        binding.expandedHeaderView.headerSubtitle.text =
            requireContext().getString(R.string.undone_count_format, count)
    }

    private fun cleanUndoneCount() {
        binding.expandedHeaderView.headerSubtitle.text = ""
    }

    private fun setupUndoneCountObserver() {
        viewModel.countDoneLiveData.observe(viewLifecycleOwner) { count ->
            if (count != null) setUndoneCount(count)
            else cleanUndoneCount()
        }
    }

    private fun setupShowHideDoneButton() {
        val listener = View.OnClickListener { viewModel.toggleShowDone() }
        binding.expandedHeaderView.showHideDoneButton.setOnClickListener(listener)
        binding.collapsedHeaderView.showHideDoneButton.setOnClickListener(listener)

        viewModel.showDone.observe(viewLifecycleOwner) { showDone ->
            val icon: Drawable? = if (showDone) {
                ContextCompat.getDrawable(requireContext(), R.drawable.visibility_off)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.visibility)
            }
            icon?.let {
                binding.expandedHeaderView.showHideDoneButton.setImageDrawable(it)
                binding.collapsedHeaderView.showHideDoneButton.setImageDrawable(it)
            }
        }
    }

    private fun initTodoItemAdapter(): TodoItemListAdapter {
        val itemClickListener: (UUID) -> Unit = { id ->
            openDetailsFragment(id, false)
        }
        val doneCheckBoxListener: (UUID, Boolean) -> Unit = { id, isDone ->
            viewModel.updateTodoItemDoneStatus(id, isDone)
        }
        val moveItemListener: (fromId: UUID, toId: UUID) -> Unit = { fromId, toId ->
            viewModel.moveTodoItemsInList(fromId, toId)
        }
        val deleteItemListener: (UUID) -> Unit = { id ->
            viewModel.deleteTodoItem(id)
        }
        val showUndoDeletionSnackbar: (() -> Unit) -> Unit = { updateUI ->
            val snackbar = Snackbar.make(
                binding.itemListCoordinatorLayout,
                R.string.undo_deletion_snackbar,
                Snackbar.LENGTH_SHORT
            )
            snackbar.setAction(R.string.restore_todo) {
                viewModel.undoTodoItemDeletion()
                updateUI()
            }
            snackbar.show()
        }

        return TodoItemListAdapter(
            itemClickListener,
            doneCheckBoxListener,
            moveItemListener,
            deleteItemListener,
            showUndoDeletionSnackbar
        )
    }

    private fun setupRecyclerView(
        recyclerViewAdapter: TodoItemListAdapter
    ) {
        val itemAnimator = object : DefaultItemAnimator() {
            //override to prevent shimmering animation when outermost items deleted
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder) = true
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = recyclerViewAdapter
            this.itemAnimator = itemAnimator
        }
        recyclerViewAdapter.itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    private fun submitRecyclerViewAdapterList(itemList: List<TodoItemDomainEntity>) {
        (binding.recyclerView.adapter as TodoItemListAdapter).submitList(itemList)

        if (itemList.isEmpty()) {
            binding.apply {
                noItemsMessage.visibility = View.VISIBLE
                appBarLayout.setExpanded(false)
                recyclerView.isNestedScrollingEnabled = false
            }
            appBarLayoutBehavior?.setDragCallback(object : DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
        } else {
            binding.apply {
                noItemsMessage.visibility = View.GONE
                recyclerView.isNestedScrollingEnabled = true
            }
            appBarLayoutBehavior?.setDragCallback(object : DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return true
                }
            })
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()

        if (isHideToolbarView && percentage == 1f) {
            binding.collapsedHeaderView.listHeaderCollapsed.visibility = View.VISIBLE
            isHideToolbarView = !isHideToolbarView
        } else if (!isHideToolbarView && percentage < 1f) {
            binding.collapsedHeaderView.listHeaderCollapsed.visibility = View.GONE
            isHideToolbarView = !isHideToolbarView
        }
    }
}
