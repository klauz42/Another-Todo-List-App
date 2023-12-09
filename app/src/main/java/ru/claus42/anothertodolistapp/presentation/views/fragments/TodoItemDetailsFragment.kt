package ru.claus42.anothertodolistapp.presentation.views.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemDetailsBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemDetailsViewModel
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


private const val DIALOG_SAVE_CONFIRM = "SaveConfirmation"
private const val DIALOG_DELETE_CONFIRM = "DeleteConfirmation"

class TodoItemDetailsFragment :
    Fragment(),
    SaveConfirmationDialogFragment.SaveConfirmationListener,
    DeleteConfirmationDialogFragment.DeleteConfirmationListener {

    private lateinit var detailsFragmentComponent: FragmentComponent
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    private var _binding: FragmentTodoItemDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: TodoItemDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TodoItemDetailsViewModel by viewModels { viewModelFactory }

    private var descriptionWatcher: DescriptionWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsFragmentComponent =
            DaggerFragmentComponent.builder().activityComponent(activity.activityComponent).build()
        detailsFragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            back()
        }

        with(args) {
            viewModel.initialize(
                UUID.fromString(itemId),
                isNewItem,
            )
        }

        return view
    }

    @SuppressLint("NullSafeMutableLiveData")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupItemObservers()
        setupScrollViewListener()

        setupDeleteButton()

        descriptionWatcher = DescriptionWatcher()
        descriptionWatcher?.let { setupDescriptionTextEdit(it) }
    }

    override fun onDestroyView() {
        _binding = null
        descriptionWatcher = null

        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        binding.content.textInputLayout.editText?.selectionStart?.let {
            viewModel.descriptionCursorPosition = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.descriptionCursorPosition?.let {
            binding.content.textInputLayout.editText?.setSelection(it)
        }

    }

    private fun displayError(exception: Throwable) {
        //todo: error displaying
    }

    private fun displayLoading() {
        //todo: display loading animation
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun back() {
        if (viewModel.isItemChanged) {
            SaveConfirmationDialogFragment().show(parentFragmentManager, DIALOG_SAVE_CONFIRM)
        } else {
            if (viewModel.isNewItem) {
                viewModel.deleteTodoItem()
            }
            findNavController().navigateUp()
        }
    }

    private fun save() {
        viewModel.saveChanges()
    }

    private fun setupMenu() {
        binding.detailsHeader.backButton.setOnClickListener {
            back()
        }

        binding.detailsHeader.saveButton.setOnClickListener {
            save()
        }
    }

    private fun setupItemObservers() {
        viewModel.todoItemResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Error -> displayError(result.error)
                is DataResult.Loading -> displayLoading()
                else -> {}
            }
        }

        viewModel.todoItem.observe(viewLifecycleOwner) { item ->
            item?.let { updateUI(it) }
        }
    }

    private fun setupDescriptionTextEdit(watcher: TextWatcher) {
        binding.content.taskDescriptionEditText.apply {
            addTextChangedListener(watcher)
            setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus)
                    hideKeyboard(v)
            }
        }
    }

    private fun setupScrollViewListener() {
        binding.detailsNestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (!viewModel.isTextEditScrolledDown && scrollY > 0) {
                binding.appBarLayout.elevation =
                    requireContext().resources.getDimension(R.dimen.header_elevation)
                viewModel.isTextEditScrolledDown = true
            } else if (viewModel.isTextEditScrolledDown && scrollY == 0) {
                binding.appBarLayout.elevation = 0f
                viewModel.isTextEditScrolledDown = false
            }
        }
    }

    private fun setupDeleteButton() {
        binding.content.deleteItem.setOnClickListener {
            DeleteConfirmationDialogFragment().show(parentFragmentManager, DIALOG_DELETE_CONFIRM)
        }
    }

    private fun updateUI(newItem: TodoItemDomainEntity) {
        binding.content.apply {
            taskDescriptionEditText.setText(newItem.description)
            //todo: complete filling out UI
        }
    }

    private inner class DescriptionWatcher() : TextWatcher {
        override fun beforeTextChanged(
            text: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            viewModel.todoItem.value?.let {
                with(it) {
                    if (description != text.toString()) {
                        description = text.toString()
                        changedAt = LocalDateTime.now()
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    override fun onSaveConfirmed() {
        save()

        findNavController().navigateUp()
    }

    override fun onExitWithoutSaving() {
        if (viewModel.isNewItem) {
            viewModel.deleteTodoItem()
        }
        findNavController().navigateUp()
    }

    override fun onSaveCancel() {}

    override fun onDeleteConfirmed() {
        viewModel.todoId?.let {
            viewModel.deleteTodoItem()
        }

        findNavController().navigateUp()
    }

    override fun onDeleteCancel() {}
}