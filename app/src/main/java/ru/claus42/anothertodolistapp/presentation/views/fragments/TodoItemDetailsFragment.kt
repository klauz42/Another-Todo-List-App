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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.appComponent
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemDetailsBinding
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemDetailsViewModel
import java.time.LocalDateTime
import java.util.UUID

private const val DIALOG_SAVE_CONFIRM = "SaveConfirmation"
private const val DIALOG_DELETE_CONFIRM = "DeleteConfirmation"

class TodoItemDetailsFragment :
    Fragment(),
    SaveConfirmationDialogFragment.SaveConfirmationListener,
    DeleteConfirmationDialogFragment.DeleteConfirmationListener {

    private var _binding: FragmentTodoItemDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: TodoItemDetailsFragmentArgs by navArgs()

    private var isItemLoad: Boolean = false
    private var initialItem: TodoItemDomainEntity? = null
    private val isItemChanged get() = isItemLoad && (item.value != initialItem)

    private var item: MutableLiveData<TodoItemDomainEntity> = MutableLiveData()
    private var itemId: UUID? = null

    private var descriptionWatcher: DescriptionWatcher? = null

    private var isTextEditScrolledDown = false

    private val viewModel: TodoItemDetailsViewModel by viewModels {
        activity.appComponent.viewModelsFactory()
    }

    private val activity: AppCompatActivity by lazy { requireActivity() as AppCompatActivity }

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

        args.itemId?.let {
            itemId = UUID.fromString(it)
            viewModel.loadTodoItem(itemId!!)
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
        if (isItemChanged) {
            SaveConfirmationDialogFragment().show(parentFragmentManager, DIALOG_SAVE_CONFIRM)
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setupMenu() {
        binding.detailsHeader.backButton.setOnClickListener {
            back()
        }

        binding.detailsHeader.saveButton.setOnClickListener {
            item.value?.let {
                initialItem = it.copy()
                viewModel.updateTodoItem(it)
            }
        }
    }

    private fun setupItemObservers() {
        viewModel.todoItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Success -> {
                    item.value = result.data.also {
                        if (!isItemLoad) {
                            initialItem = it.copy()
                            isItemLoad = true
                        }
                    }
                }

                is DataResult.Error -> displayError(result.error)
                is DataResult.Loading -> displayLoading()
                else -> {}
            }
        }

        item.observe(viewLifecycleOwner) { item ->
            updateUI(item)
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
            if (!isTextEditScrolledDown && scrollY > 0) {
                binding.appBarLayout.elevation =
                    requireContext().resources.getDimension(R.dimen.header_elevation)
                isTextEditScrolledDown = true
            } else if (isTextEditScrolledDown && scrollY == 0) {
                binding.appBarLayout.elevation = 0f
                isTextEditScrolledDown = false
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
        }

        override fun afterTextChanged(s: Editable?) {
            item.value?.let {
                with(it) {
                    if (description != s.toString()) {
                        description = s.toString()
                        changedAt = LocalDateTime.now()
                    }
                }
            }
        }

    }

    override fun onSaveConfirmed() {
        item.value?.let {
            viewModel.updateTodoItem(it)
        }

        findNavController().navigateUp()
    }

    override fun onExitWithoutSaving() {
        findNavController().navigateUp()
    }

    override fun onSaveCancel() {}

    override fun onDeleteConfirmed() {
        item.value?.let {
            viewModel.deleteTodoItem(it.id)
        }

        findNavController().navigateUp()
    }

    override fun onDeleteCancel() {}
}