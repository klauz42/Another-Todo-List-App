package ru.claus42.anothertodolistapp.tests.robots

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers
import ru.claus42.anothertodolistapp.R


class TodoItemListTestRobot : BaseTestRobot() {

    fun scrollToToDoWithDescription(description: String) {
        scrollToMatchingItem(
            R.id.recycler_view,
            ViewMatchers.hasDescendant(ViewMatchers.withText(description))
        )
    }

    fun scrollToToDoWithPosition(position: Int) {
        onView(ViewMatchers.withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
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

    fun checkToDoDoesNotExist(description: String) {
        checkItemDoesNotExist(
            R.id.recycler_view,
            withText(description),
        )
    }

    companion object {
        fun todoItemList(func: TodoItemListTestRobot.() -> Unit) = TodoItemListTestRobot()
            .apply { func() }
    }
}