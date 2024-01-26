package ru.claus42.anothertodolistapp.tests

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.claus42.anothertodolistapp.tests.idlingresources.RecyclerViewIdlingResource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.claus42.anothertodolistapp.MainApp
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments.TodoItemListFragment
import ru.claus42.anothertodolistapp.tests.robots.TodoItemDetailsTestRobot.Companion.todoItemDetails
import ru.claus42.anothertodolistapp.tests.robots.TodoItemListTestRobot.Companion.todoItemList
import ru.claus42.anothertodolistapp.tests.utils.generateTodoItems

@RunWith(AndroidJUnit4::class)
class TodoItemsUITest {

    @Volatile
    private var recyclerViewIdlingResource: RecyclerViewIdlingResource? = null
    private val repository get() = application.appComponent.getTodoItemRepository()

    @Before
    fun setUp() {
        runBlocking {
            initDomainItems.forEach { item ->
                repository.addItem(item)
            }
        }

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.onActivity { activity ->
            val navHostFragment = activity
                                    .supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment)

            var itemListFragment: TodoItemListFragment? = null
            for (fragment in navHostFragment!!.childFragmentManager.fragments) {
                if (fragment is TodoItemListFragment) {
                    itemListFragment = fragment
                }
            }

            val recyclerView = itemListFragment?.binding?.recyclerView
            recyclerView?.let {
                val itemCount = initDomainItems.size
                    recyclerViewIdlingResource = RecyclerViewIdlingResource(it, itemCount).apply {
                    IdlingRegistry.getInstance().register(this)
                }
            }
        }
    }

    @After
    fun tearDown() = runBlocking {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
        repository.clearRepository()
    }

    @Test
    fun shouldOpenItemDetailsAfterClickOnInfoImageButton() {
        val position = 15
        val deltaToScrollFab = 3
        val description = initDomainItems[(initDomainItems.size - 1) - position].description

        todoItemList {
            scrollToToDoWithPosition(position + deltaToScrollFab)
            openToDoWithDescription(description)
        }

        todoItemDetails {
            checkIfToDoIsOpen()
            checkIfToDoDescriptionIsCorrect(description)
        }
    }

    @Test
    fun shouldDeleteToDoAfterClickOnDeleteButtonOnDetailsScreen() {
        val position = 20
        val deltaToScrollFab = 3
        val description = initDomainItems[(initDomainItems.size - 1) - position].description

        todoItemList {
            scrollToToDoWithPosition(position + deltaToScrollFab)
            openToDoWithDescription(description)
        }

        recyclerViewIdlingResource?.decrementItemCount()

        todoItemDetails {
            clickDelete()
            confirmDeletion()
        }

        todoItemList {
            checkToDoDoesNotExist(description)
        }
    }

    companion object {
        private val application = ApplicationProvider.getApplicationContext<Context>() as MainApp
        private val initDomainItems = generateTodoItems()
    }
}