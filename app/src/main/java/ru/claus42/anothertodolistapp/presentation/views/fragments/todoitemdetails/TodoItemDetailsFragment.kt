package ru.claus42.anothertodolistapp.presentation.views.fragments.todoitemdetails

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.TimePicker
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
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
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.viewmodels.TodoItemDetailsViewModel
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID
import javax.inject.Inject


private const val DIALOG_SAVE_CONFIRM = "SaveConfirmation"
private const val DIALOG_DELETE_CONFIRM = "DeleteConfirmation"
private const val TIME_PICKER = "TimePicker"
private const val DATE_PICKER = "DatePicker"

class TodoItemDetailsFragment :
    Fragment(),
    SaveConfirmationDialogFragment.SaveConfirmationListener,
    DeleteConfirmationDialogFragment.DeleteConfirmationListener,
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var detailsFragmentComponent: FragmentComponent
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    private var _binding: FragmentTodoItemDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: TodoItemDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TodoItemDetailsViewModel by viewModels { viewModelFactory }

    private var descriptionWatcher: DescriptionWatcher? = null

    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    private fun LocalDateTime.getFormatted(): String {
        return this.format(formatter)
    }

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
        setupDescriptionTextWatcher()
        setupPriorityPopupMenu()
        setupDeadlineViews()

    }

    override fun onDestroyView() {
        _binding = null
        descriptionWatcher = null

        super.onDestroyView()
    }

    private fun getCursorPosition() = binding.content.textInputLayout.editText?.selectionStart
    private fun setCursorPosition(position: Int) {
        binding.content.textInputLayout.editText?.setSelection(position)
    }

    override fun onPause() {
        super.onPause()
        viewModel.descriptionCursorPosition = getCursorPosition()
    }

    override fun onResume() {
        super.onResume()
        viewModel.descriptionCursorPosition?.let { setCursorPosition(it) }
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

    //todo: add deadline date comparison
    private fun shouldUpdateUI(item: TodoItemDomainEntity): Boolean {
        return with(binding.content) {
            priorityChoose.text != getString(item.itemPriority.toStringResId())
                    || taskDescriptionEditText.text.toString() != viewModel.getDescription()
                    || deadlineDate.text != viewModel.getDeadline()?.format(formatter)
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
            item?.let {
                if (shouldUpdateUI(it)) {
                    updateUI(it)
                }
            }
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

    private fun setupDescriptionTextWatcher() {
        descriptionWatcher = DescriptionWatcher()
        descriptionWatcher?.let {
            setupDescriptionTextEdit(it)
        }
    }

    private fun setupPriorityPopupMenu() {
        binding.content.priorityChooseLayout.setOnClickListener { v ->
            //todo: try to use ListPopupWindow class
            val menu = PopupMenu(requireContext(), v)
            menu.inflate(R.menu.dropdown_menu_item_priority)
            menu.setOnMenuItemClickListener {
                it.itemId
                it.itemId.toPriority()?.let { newPriority ->
                    viewModel.setPriority(newPriority)
                }
                true
            }
            menu.show()
        }
    }

    private fun showDatePickerDialog() {
        val dateTime = viewModel.getDeadline()
        dateTime?.let {
            val datePicker = DatePickerDialogFragment
                .newInstance(it.year, it.monthValue, it.dayOfMonth)
            datePicker.show(parentFragmentManager, DATE_PICKER)
        }
    }

    private fun showTimePickerDialog() {
        val dateTime = viewModel.getDeadline()
        dateTime?.let {
            val timePicker = TimePickerDialogFragment.newInstance(it.hour, it.minute)
            timePicker.show(parentFragmentManager, TIME_PICKER)
        }
    }

    private fun updateDeadlineTextView(isEnabled: Boolean) {
        binding.content.apply {
            val color =
                if (isEnabled) R.color.deadline_date_selector else R.color.deadline_off_color

            deadlineDate.setTextColor(ContextCompat.getColorStateList(requireContext(), color))
            deadlineDate.isClickable = isEnabled

            if (isEnabled) {
                deadlineDate.setOnClickListener {
                    showDatePickerDialog()
                }
            } else {
                deadlineDate.setOnClickListener(null)
            }
        }
    }

    private fun setupDeadlineViews() {
        binding.content.apply {
            deadlineSwitch.setOnCheckedChangeListener { switch, isChecked ->
                updateDeadlineTextView(isChecked)
                viewModel.updateDeadlineIsEnabled(isChecked)
            }
        }
    }

    private fun updateUI(newItem: TodoItemDomainEntity) {
        binding.content.apply {
            val cursorPosition = getCursorPosition()
            taskDescriptionEditText.setText(newItem.description)
            cursorPosition?.let { setCursorPosition(cursorPosition) }

            priorityChoose.setText(newItem.itemPriority.toStringResId())

            newItem.isDeadlineEnabled.let {
                deadlineSwitch.isChecked = it
                updateDeadlineTextView(it)
            }

            deadlineDate.text = newItem.deadline.getFormatted()
        }
    }

    private fun ItemPriority.toStringResId(): Int {
        return when (this) {
            ItemPriority.LOW -> R.string.low_priority
            ItemPriority.BASIC -> R.string.none_priority
            ItemPriority.IMPORTANT -> R.string.important_priority
        }
    }

    private fun Int.toPriority(): ItemPriority? {
        return when (this) {
            R.id.option_basic -> ItemPriority.BASIC
            R.id.option_low -> ItemPriority.LOW
            R.id.option_important -> ItemPriority.IMPORTANT
            else -> null
        }
    }


    private inner class DescriptionWatcher() : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.updateDescription(text.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //month + 1 because DatePicker numbers month from 0 and LocalDate from 1
        viewModel.updateDeadlineDate(year, month + 1, dayOfMonth)
        showTimePickerDialog()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        viewModel.updateDeadlineTime(hour, minute)
    }
}