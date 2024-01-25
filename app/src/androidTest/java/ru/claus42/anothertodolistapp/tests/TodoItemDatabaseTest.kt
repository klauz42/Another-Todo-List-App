package ru.claus42.anothertodolistapp.tests

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import ru.claus42.anothertodolistapp.data.local.TodoItemDao
import ru.claus42.anothertodolistapp.data.local.TodoItemDatabase
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.local.models.mappers.toLocalDataModel
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.tests.utils.generateTodoItems

@RunWith(AndroidJUnit4::class)
class TodoItemDatabaseTest {

    private val itemsListSize = 10
    private val initItems = generateTodoItems(itemsListSize)

    @Before
    fun fillDb() {
        initItems.map { it.toLocalDataModel() }.forEach { item ->
            runBlocking {
                dao.addTodoItem(0, item)
            }
        }
    }

    @After
    fun clearDb() = runBlocking {
        dao.clearTable()
    }


    @Test
    fun shouldBeCorrectIndicesAfterMovingFirstElementToLastPosition() {
        val modifiedItemList: List<TodoItemLocalDataEntity>

        val initItems = runBlocking {
            dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        val fromIndex = 0
        val toIndex = itemsListSize - 1
        val fromId = initItems[fromIndex].id
        val toId = initItems[toIndex].id

        runBlocking {
            dao.moveItem(fromId, toId)
            modifiedItemList = dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        for (i in 0 until itemsListSize) {
            assertEquals(initItems[(i + 1) % itemsListSize].id, modifiedItemList[i].id)
        }
    }

    @Test
    fun shouldBeCorrectIndicesAfterMovingLastElementToFirstPosition() {
        val modifiedItemList: List<TodoItemLocalDataEntity>

        val initItems = runBlocking {
            dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        val fromIndex = itemsListSize - 1
        val toIndex = 0
        val fromId = initItems[fromIndex].id
        val toId = initItems[toIndex].id

        runBlocking {
            dao.moveItem(fromId, toId)
            modifiedItemList = dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        for (i in 0 until itemsListSize) {
            assertEquals(initItems[i].id, modifiedItemList[(i + 1) % itemsListSize].id)
        }
    }

    @Test
    fun shouldBeCorrectListSizeAndIndicesAfterDeletion() {
        val modifiedItemList: List<TodoItemLocalDataEntity>

        val initItems = runBlocking {
            dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        runBlocking {
            dao.deleteTodoItem(initItems[itemsListSize / 2])
            modifiedItemList = dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        assertEquals(itemsListSize - 1, modifiedItemList.size)

        for (i in 0 until itemsListSize - 1) {
            assertEquals(i.toLong(), modifiedItemList[i].orderIndex)
        }
    }

    @Test
    fun shouldBeCorrectListSizeAndIndicesAfterAddition() {
        val modifiedItemList: List<TodoItemLocalDataEntity>

        runBlocking {
            dao.addTodoItem(0, TodoItemDomainEntity().toLocalDataModel())
            modifiedItemList = dao.getTodoItems().first().sortedBy { it.orderIndex }
        }

        assertEquals(itemsListSize + 1, modifiedItemList.size)

        for (i in 0 until itemsListSize + 1) {
            assertEquals(i.toLong(), modifiedItemList[i].orderIndex)
        }
    }

    companion object {
        private lateinit var db: TodoItemDatabase
        private lateinit var dao: TodoItemDao

        @BeforeClass
        @JvmStatic
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(context, TodoItemDatabase::class.java)
                .allowMainThreadQueries().build()
            dao = db.todoItemDao()

            runBlocking {
                dao.clearTable()
            }
        }

        @AfterClass
        @JvmStatic
        fun closeDb() {
            db.close()
        }
    }
}