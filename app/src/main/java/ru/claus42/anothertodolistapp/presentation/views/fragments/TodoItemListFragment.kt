package ru.claus42.anothertodolistapp.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemListBinding
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.adapters.TodoItemAdapter
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemListViewModel
import kotlin.math.abs

class TodoItemListFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentTodoItemListBinding? = null
    private val binding get() = _binding!!


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

        setupAppBar()
        setupRecyclerView()

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

    override fun onDestroyView() {
        _binding = null

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

    private fun setupRecyclerView() {
        val adapter = TodoItemAdapter { id ->
            val idString = id.toString()
            val action = TodoItemListFragmentDirections.actionListToDetails(idString)
            findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun submitRecyclerViewAdapterList(itemList: List<TodoItemDomainEntity>) =
        (binding.recyclerView.adapter as TodoItemAdapter).submitList(itemList)

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
