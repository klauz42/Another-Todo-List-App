package ru.claus42.anothertodolistapp.presentation.searchtodos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.claus42.anothertodolistapp.databinding.FragmentSortOptionsBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.SortOptionsViewModel
import javax.inject.Inject

class SortOptionsFragment : Fragment() {
    private lateinit var fragmentComponent: FragmentComponent
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    private var _binding: FragmentSortOptionsBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SortOptionsViewModel by viewModels { viewModelFactory }

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
        _binding = FragmentSortOptionsBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        return binding.root
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.dateSortType.observe(viewLifecycleOwner) { dateSortType ->
                typeDateToSort.check(
                    when (dateSortType) {
                        DateSortType.CREATION -> creationDate.id
                        DateSortType.CHANGED -> changeDate.id
                    }
                )
            }
            viewModel.dateSortValue.observe(viewLifecycleOwner) { dateSortValue ->
                sortByDate.check(
                    when (dateSortValue) {
                        DateSortValue.NEW_ONES_FIRST -> newFirst.id
                        DateSortValue.OLD_ONES_FIRST -> oldFirst.id
                    }
                )
            }
            viewModel.deadlineSortValue.observe(viewLifecycleOwner) { dateSortValue ->
                sortByDeadline.check(
                    when (dateSortValue) {
                        DeadlineSortValue.EARLIER_FIRST -> earlierDeadlineFirst.id
                        DeadlineSortValue.LATER_FIRST -> laterDeadlineFirst.id
                        DeadlineSortValue.NO_DEADLINE_SORT -> noSortByDeadline.id
                    }
                )
            }
            viewModel.importanceSortValue.observe(viewLifecycleOwner) { dateSortValue ->
                sortByImportance.check(
                    when (dateSortValue) {
                        ImportanceSortValue.MOST_IMPORTANT_FIRST -> mostImportantFirst.id
                        ImportanceSortValue.LESS_IMPORTANT_FIRST -> lessImportantFirst.id
                        ImportanceSortValue.NO_IMPORTANCE_SORT -> noSortByImportance.id
                    }
                )
            }

            typeDateToSort.setOnCheckedChangeListener { group, checkedId ->
                viewModel.setDateSortType(
                    when (checkedId) {
                        creationDate.id -> DateSortType.CREATION
                        changeDate.id -> DateSortType.CHANGED
                        else -> DateSortType.CREATION
                    }
                )
            }
            sortByDate.setOnCheckedChangeListener { group, checkedId ->
                viewModel.setDateSortValue(
                    when (checkedId) {
                        newFirst.id -> DateSortValue.NEW_ONES_FIRST
                        oldFirst.id -> DateSortValue.OLD_ONES_FIRST
                        else -> DateSortValue.NEW_ONES_FIRST
                    }
                )
            }
            sortByDeadline.setOnCheckedChangeListener { group, checkedId ->
                viewModel.setDeadlineSortValue(
                    when (checkedId) {
                        earlierDeadlineFirst.id -> DeadlineSortValue.EARLIER_FIRST
                        laterDeadlineFirst.id -> DeadlineSortValue.LATER_FIRST
                        else -> DeadlineSortValue.NO_DEADLINE_SORT
                    }
                )
            }
            sortByImportance.setOnCheckedChangeListener { group, checkedId ->
                viewModel.setImportanceSortValue(
                    when (checkedId) {
                        mostImportantFirst.id -> ImportanceSortValue.MOST_IMPORTANT_FIRST
                        lessImportantFirst.id -> ImportanceSortValue.LESS_IMPORTANT_FIRST
                        else -> ImportanceSortValue.NO_IMPORTANCE_SORT
                    }
                )
            }
        }
    }

    companion object {
        private const val TAG = "SortOptionsFragment"
    }
}