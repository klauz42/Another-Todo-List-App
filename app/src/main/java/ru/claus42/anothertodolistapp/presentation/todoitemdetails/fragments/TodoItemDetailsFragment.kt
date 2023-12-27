package ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.claus42.anothertodolistapp.databinding.FragmentTodoItemDetailsBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments.viewcontroller.TodoItemDetailsViewController
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.stateholders.TodoItemDetailsViewModel
import java.util.UUID
import javax.inject.Inject


class TodoItemDetailsFragment :
    Fragment(),
    SaveConfirmationDialogFragment.SaveConfirmationListener,
    DeleteConfirmationDialogFragment.DeleteConfirmationListener,
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener
{
    private lateinit var detailsFragmentComponent: FragmentComponent
    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    private var _binding: FragmentTodoItemDetailsBinding? = null
    val binding get() = _binding!!
    private val args: TodoItemDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: TodoItemDetailsViewModel by viewModels { viewModelFactory }

    private val viewController: TodoItemDetailsViewController by lazy {
        TodoItemDetailsViewController(this)
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

        viewController.initialize()
    }

    override fun onDestroyView() {
        _binding = null
        viewController.release()

        super.onDestroyView()
    }

    fun getCursorPosition() = binding.content.textInputLayout.editText?.selectionStart
    fun setCursorPosition(position: Int) {
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

    fun save() {
        viewModel.saveChanges()
    }

    fun back() {
        if (viewModel.isItemChanged) {
            SaveConfirmationDialogFragment().show(parentFragmentManager, DIALOG_SAVE_CONFIRM)
        } else {
            if (viewModel.isNewItem) {
                viewModel.deleteTodoItem()
            }
            findNavController().navigateUp()
        }
    }

    fun showDatePickerDialog() {
        val dateTime = viewModel.getDeadline()
        dateTime?.let {
            val datePicker =
                DatePickerDialogFragment.newInstance(it.year, it.monthValue, it.dayOfMonth)
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

    companion object {
        const val DIALOG_SAVE_CONFIRM = "SaveConfirmation"
        const val DIALOG_DELETE_CONFIRM = "DeleteConfirmation"
        const val TIME_PICKER = "TimePicker"
        const val DATE_PICKER = "DatePicker"
    }
}