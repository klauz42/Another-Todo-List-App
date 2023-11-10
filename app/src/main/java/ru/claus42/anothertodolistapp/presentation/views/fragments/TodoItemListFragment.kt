package ru.claus42.anothertodolistapp.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemListBinding
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.adapters.TodoItemAdapter
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemListViewModel
import java.util.UUID
import kotlin.math.abs

class TodoItemListFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentTodoItemListBinding? = null
    private val binding get() = _binding!!


    private val viewModel: TodoItemListViewModel by viewModels {
        activity.appComponent.viewModelsFactory()
    }

    private var isHideToolbarView: Boolean = false

    private var appBarLayoutBehavior: AppBarLayout.Behavior? = null

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

        setupAppBar()
        setupRecyclerView()
        setupAppbarLayoutParams()
        setupItemsObserver()
        setupShowHideDoneButton()
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

    //todo: add set show/hide done button listener
    private fun setupShowHideDoneButton() {
        binding.expandedHeaderView.showHideDoneButton.setOnClickListener {

        }
        binding.collapsedHeaderView.showHideDoneButton.setOnClickListener {

        }
    }

    private fun setupRecyclerView() {
        val itemClickListener: (UUID) -> Unit = { id ->
            val idString = id.toString()
            val action = TodoItemListFragmentDirections.actionListToDetails(idString)
            findNavController().navigate(action)
        }
        val doneCheckBoxListener: (UUID, Boolean) -> Unit = { id, isDone ->
            viewModel.updateTodoItemDoneStatus(id, isDone)
        }

        val adapter = TodoItemAdapter(
            itemClickListener,
            doneCheckBoxListener
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun submitRecyclerViewAdapterList(itemList: List<TodoItemDomainEntity>) {
        (binding.recyclerView.adapter as TodoItemAdapter).submitList(itemList)

        if (itemList.isEmpty()) {
            binding.noItemsMessage.visibility = View.VISIBLE
            binding.appBarLayout.setExpanded(false)
            binding.recyclerView.isNestedScrollingEnabled = false
            appBarLayoutBehavior?.setDragCallback(object : DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return false
                }
            })
        } else {
            binding.noItemsMessage.visibility = View.GONE
            binding.recyclerView.isNestedScrollingEnabled = true
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
