package ru.claus42.anothertodolistapp.presentation.searchtodos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.claus42.anothertodolistapp.databinding.FragmentSearchTodosBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.searchtodos.adapters.SearchResultListAdapter
import ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.viewcontroller.SearchTodosViewController
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.SearchTodosViewModel
import javax.inject.Inject

class SearchTodosFragment : Fragment() {
    private var _binding: FragmentSearchTodosBinding? = null
    val binding get() = _binding!!

    private lateinit var fragmentComponent: FragmentComponent
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SearchTodosViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var recyclerViewAdapter: SearchResultListAdapter

    private val viewController: SearchTodosViewController by
    lazy { SearchTodosViewController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent =
            DaggerFragmentComponent.builder().activityComponent(activity.activityComponent).build()
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchTodosBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewController.initialize()
    }

    override fun onDestroy() {
        _binding = null

        super.onDestroy()
    }
}