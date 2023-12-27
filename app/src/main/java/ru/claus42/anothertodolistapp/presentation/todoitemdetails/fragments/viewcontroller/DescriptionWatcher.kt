package ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments.viewcontroller

import android.text.Editable
import android.text.TextWatcher
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.stateholders.TodoItemDetailsViewModel


class DescriptionWatcher(
    private val viewModel: TodoItemDetailsViewModel
) : TextWatcher {
    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        viewModel.updateDescription(text.toString())
    }

    override fun afterTextChanged(s: Editable?) {}
}