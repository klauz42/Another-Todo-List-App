package ru.claus42.anothertodolistapp.tests.robots

import androidx.test.espresso.matcher.ViewMatchers.withId
import ru.claus42.anothertodolistapp.R

class TodoItemDetailsTestRobot : BaseTestRobot() {

    fun checkIfToDoIsOpen() {
        isMatchingItemDisplayed(withId(R.id.details_nested_scroll_view))
    }

    fun checkIfToDoDescriptionIsCorrect(description: String) {
        matchText(R.id.task_description_edit_text, description)
    }

    companion object {
        fun todoItemDetails(func: TodoItemDetailsTestRobot.() -> Unit) = TodoItemDetailsTestRobot()
            .apply { func() }
    }
}