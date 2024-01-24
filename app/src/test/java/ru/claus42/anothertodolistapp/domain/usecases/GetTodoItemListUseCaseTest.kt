package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository


class GetTodoItemListUseCaseTest {
    private val domainItems = generateTodoItems()

    @Test
    operator fun invoke() = runTest {
        val todoItemsRepository: TodoItemRepository = mock<TodoItemRepository> {
            on { getTodoItems() } doReturn flowOf(DataResult.Success(domainItems))
        }

        val getTodoItemListUseCase = GetTodoItemListUseCase(todoItemsRepository)
        val result = getTodoItemListUseCase().first()

        assertTrue(result is DataResult.Success)

        val list = (result as DataResult.Success).data

        assertTrue(domainItems == list)
    }
}