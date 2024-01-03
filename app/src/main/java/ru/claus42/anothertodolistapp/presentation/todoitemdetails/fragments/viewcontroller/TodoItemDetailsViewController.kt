package ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments.viewcontroller

import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments.DeleteConfirmationDialogFragment
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments.TodoItemDetailsFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject


@FragmentScope
class TodoItemDetailsViewController @Inject constructor(
    private val fragment: TodoItemDetailsFragment,
) {
    private var descriptionWatcher: DescriptionWatcher? = null

    private val binding get() = fragment.binding
    private val viewModel get() = fragment.viewModel
    private val activity: MainActivity by lazy { fragment.requireActivity() as MainActivity }
    private val viewLifecycleOwner get() = fragment.viewLifecycleOwner
    private val context: Context by lazy { fragment.requireContext() }
    private val parentFragmentManager get() = fragment.parentFragmentManager

    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    private fun LocalDateTime.getFormatted(): String {
        return this.format(formatter)
    }

    fun initialize() {
        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            fragment.back()
        }

        setupMenu()
        setupItemObservers()
        setupScrollViewListener()
        setupDeleteButton()
        setupDescriptionTextWatcher()
        setupPriorityPopupMenu()
        setupDeadlineViews()
    }

    fun release() {
        descriptionWatcher = null
    }

    private fun setupMenu() {
        binding.detailsHeader.backButton.setOnClickListener {
            fragment.back()
        }

        binding.detailsHeader.saveButton.setOnClickListener {
            fragment.save()
        }
    }

    private fun setupScrollViewListener() {
        binding.detailsNestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (!viewModel.isTextEditScrolledDown && scrollY > 0) {
                binding.appBarLayout.elevation =
                    context.resources.getDimension(R.dimen.header_elevation)
                viewModel.isTextEditScrolledDown = true
            } else if (viewModel.isTextEditScrolledDown && scrollY == 0) {
                binding.appBarLayout.elevation = 0f
                viewModel.isTextEditScrolledDown = false
            }
        }
    }

    private fun setupDeleteButton() {
        binding.content.deleteItem.setOnClickListener {
            DeleteConfirmationDialogFragment().show(
                parentFragmentManager,
                TodoItemDetailsFragment.DIALOG_DELETE_CONFIRM
            )
        }
    }

    private fun setupDescriptionTextWatcher() {
        descriptionWatcher = DescriptionWatcher(viewModel)
        descriptionWatcher?.let {
            setupDescriptionTextEdit(it)
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

    private fun setupPriorityPopupMenu() {
        binding.content.priorityChooseLayout.setOnClickListener { v ->
            //todo: try to use ListPopupWindow class
            val menu = PopupMenu(context, v)
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

    private fun shouldUpdateUI(item: TodoItemDomainEntity): Boolean {
        return with(binding.content) {
            priorityChoose.text != fragment.getString(item.itemPriority.toStringResId())
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

    private fun updateDeadlineTextView(isEnabled: Boolean) {
        binding.content.apply {
            val color =
                if (isEnabled) R.color.deadline_date_selector else R.color.deadline_off_color

            deadlineDate.setTextColor(ContextCompat.getColorStateList(context, color))
            deadlineDate.isClickable = isEnabled

            if (isEnabled) {
                deadlineDate.setOnClickListener {
                    fragment.showDatePickerDialog()
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
            val cursorPosition = fragment.getCursorPosition()
            taskDescriptionEditText.setText(newItem.description)
            cursorPosition?.let { fragment.setCursorPosition(cursorPosition) }

            priorityChoose.setText(newItem.itemPriority.toStringResId())

            newItem.isDeadlineEnabled.let {
                deadlineSwitch.isChecked = it
                updateDeadlineTextView(it)
            }

            deadlineDate.text = newItem.deadline.getFormatted()
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun displayError(exception: Throwable) {
        val snackbar = Snackbar.make(
            binding.detailsNestedScrollView,
            fragment.requireContext().getString(
                R.string.error_occurred_format,
                exception.toString()
            ),
            Snackbar.LENGTH_SHORT,
        )
        snackbar.show()
    }

    private fun displayLoading() {
        //todo: display loading animation
    }
}