package ru.claus42.anothertodolistapp.tests.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers
import ru.claus42.anothertodolistapp.R

class TodoItemListTestRobot : BaseTestRobot() {

    fun scrollToToDoWithDescription(description: String) {
        scrollToMatchingItem(
            R.id.recycler_view,
            ViewMatchers.hasDescendant(ViewMatchers.withText(description))
        )
    }

    fun openToDoWithDescription(description: String) {
        onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.info_icon),
                ViewMatchers.hasSibling(ViewMatchers.hasDescendant(ViewMatchers.withText(description)))
            )
        ).perform(ViewActions.click())
    }


    companion object {
        fun todoItemList(func: TodoItemListTestRobot.() -> Unit) = TodoItemListTestRobot()
            .apply { func() }
    }
}