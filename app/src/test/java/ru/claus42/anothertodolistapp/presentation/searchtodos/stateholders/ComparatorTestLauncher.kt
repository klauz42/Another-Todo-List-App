package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import org.junit.Assert
import ru.claus42.anothertodolistapp.domain.models.entities.ComparatorParameter
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.lang.reflect.Method

class ComparatorTestLauncher(
    private val comparatorMethodName: String,
    private vararg val comparatorParameterClasses: Class<out ComparatorParameter>,
) {
    private val klass = SearchTodosViewModel::class.java
    private val companion = klass.getDeclaredField("Companion")
    private val companionClass = SearchTodosViewModel.Companion::class.java
    private val companionInstance = companion.get(null)

    private fun getPrivateComparatorMethod(): Method {
        val method: Method = companionClass.getDeclaredMethod(
            comparatorMethodName,
            *comparatorParameterClasses
        )
        method.isAccessible = true
        return method
    }

    @Suppress("UNCHECKED_CAST")
    private fun comparator(vararg comparatorParameters: ComparatorParameter) =
        getPrivateComparatorMethod().invoke(
            companionInstance,
            *comparatorParameters
        ) as Comparator<TodoItemDomainEntity>

    fun launchTest(
        item1: TodoItemDomainEntity,
        item2: TodoItemDomainEntity,
        expectedResult: ComparisonResult,
        vararg comparatorParameters: ComparatorParameter,
    ) {
        val result = comparator(*comparatorParameters).compare(item1, item2)
        when (expectedResult) {
            ComparisonResult.EQUAL -> Assert.assertTrue(result == 0)
            ComparisonResult.FIRST_IS_BEFORE_SECOND -> Assert.assertTrue(result < 0)
            ComparisonResult.FIRST_IS_AFTER_SECOND -> Assert.assertTrue(result > 0)
        }
    }
}