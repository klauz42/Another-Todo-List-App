package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.claus42.anothertodolistapp.domain.usecases.EditTodoItemUseCase
import java.util.UUID
import javax.inject.Inject

class TodoItemDetailsViewModel @Inject constructor(
    private val editTodoItemDetailsUseCase: EditTodoItemUseCase
) : ViewModel() {
    fun getTodoItem(id: UUID) = editTodoItemDetailsUseCase(id)
}