package ru.claus42.anothertodolistapp.presentation.searchtodos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.claus42.anothertodolistapp.databinding.FragmentFilterOptionsBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.FilterOptionsViewModel
import javax.inject.Inject

class FilterOptionsFragment : Fragment() {
    private lateinit var fragmentComponent: FragmentComponent
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    private var _binding: FragmentFilterOptionsBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: FilterOptionsViewModel by viewModels { viewModelFactory }

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
        _binding = FragmentFilterOptionsBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.areLowPriorityIncluded.observe(viewLifecycleOwner) {
                lowPriorityOpt.isChecked = it
            }
            viewModel.areBasicPriorityIncluded.observe(viewLifecycleOwner) {
                basicPriorityOpt.isChecked = it
            }
            viewModel.areImportantPriorityIncluded.observe(viewLifecycleOwner) {
                importantPriorityOpt.isChecked = it
            }
            viewModel.areOnlyWithDeadlineIncluded.observe(viewLifecycleOwner) {
                withDeadlineOnlyOpt.isChecked = it
            }
            viewModel.areDoneIncluded.observe(viewLifecycleOwner) {
                completedOpt.isChecked = it
            }

            lowPriorityOpt.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed)
                    viewModel.setLowPriorityToDosIncluded(isChecked)
            }
            basicPriorityOpt.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed)
                    viewModel.setBasicPriorityToDosIncluded(isChecked)
            }
            importantPriorityOpt.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed)
                    viewModel.setImportantPriorityToDosIncluded(isChecked)
            }
            withDeadlineOnlyOpt.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed)
                    viewModel.setOnlyWithDeadlineToDosIncluded(isChecked)
            }
            completedOpt.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed)
                    viewModel.setDoneToDosIncluded(isChecked)
            }

        }
    }
}