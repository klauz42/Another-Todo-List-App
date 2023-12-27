package ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemListBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.itemtouchhelper.TodoItemListTouchHelperCallback
import ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.recyclerview.TodoItemListAdapter
import ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments.viewcontroller.TodoItemListViewController
import ru.claus42.anothertodolistapp.presentation.todoitemlist.stateholders.TodoItemListViewModel
import javax.inject.Inject
import kotlin.math.abs


@FragmentScope
class TodoItemListFragment : Fragment(),
    AppBarLayout.OnOffsetChangedListener {
    private lateinit var listFragmentComponent: FragmentComponent

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var helperCallback: TodoItemListTouchHelperCallback
    private val viewController: TodoItemListViewController by lazy {
        TodoItemListViewController(this, helperCallback)
    }

    @Inject
    lateinit var recyclerViewAdapter: TodoItemListAdapter

    val viewModel: TodoItemListViewModel by viewModels { viewModelFactory }

    private var _binding: FragmentTodoItemListBinding? = null
    val binding get() = _binding!!

    private var isHideToolbarView: Boolean = false

    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewController.initialize()
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
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
