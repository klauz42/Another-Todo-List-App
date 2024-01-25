package ru.claus42.anothertodolistapp.tests.robots

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher

open class BaseTestRobot {

    fun fillEditText(resId: Int, text: String): ViewInteraction {
        return onView(withId(resId)).perform(replaceText(text), closeSoftKeyboard())
    }

    fun clickButton(resId: Int): ViewInteraction = onView(withId(resId)).perform(click())

    fun textView(resId: Int): ViewInteraction = onView(withId(resId))

    fun matchText(viewInteraction: ViewInteraction, text: String): ViewInteraction {
        return viewInteraction.check(matches(withText(text)))
    }

    fun matchText(resId: Int, text: String): ViewInteraction = matchText(textView(resId), text)

    fun scrollToMatchingItem(recyclerViewResId: Int, matcher: Matcher<View>): ViewInteraction {
        return onView(withId(recyclerViewResId))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    matcher
                )
            )
    }

    fun isMatchingItemDisplayed(matcher: Matcher<View>): ViewInteraction {
        return onView(matcher).check(matches(ViewMatchers.isDisplayed()))
    }
}