package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue.LESS_IMPORTANT_FIRST
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue.MOST_IMPORTANT_FIRST
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue.NO_IMPORTANCE_SORT
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.EQUAL
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.FIRST_IS_AFTER_SECOND
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.FIRST_IS_BEFORE_SECOND


@RunWith(Parameterized::class)
class ImportantComparatorTest(
    private val firstTodo: TodoItemDomainEntity,
    private val secondTodo: TodoItemDomainEntity,
    private val expectedResult: ComparisonResult,
    private val sortValue: ImportanceSortValue
) {

    @Test
    fun shouldCompareItemsWithImportanceComparator() {
        importanceComparatorLauncher.launchTest(
            firstTodo,
            secondTodo,
            expectedResult,
            sortValue
        )
    }

    companion object {
        private val low = TodoItemDomainEntity(itemPriority = ItemPriority.LOW)
        private val basic = TodoItemDomainEntity(itemPriority = ItemPriority.BASIC)
        private val important = TodoItemDomainEntity(itemPriority = ItemPriority.IMPORTANT)

        private val importanceComparatorLauncher = ComparatorTestLauncher(
            "getImportanceComparator",
            ImportanceSortValue::class.java
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(low, low, EQUAL, NO_IMPORTANCE_SORT),
                arrayOf(basic, low, EQUAL, NO_IMPORTANCE_SORT),
                arrayOf(low, important, EQUAL, NO_IMPORTANCE_SORT),
                arrayOf(low, low, EQUAL, MOST_IMPORTANT_FIRST),
                arrayOf(basic, basic, EQUAL, MOST_IMPORTANT_FIRST),
                arrayOf(important, important, EQUAL, MOST_IMPORTANT_FIRST),
                arrayOf(low, basic, FIRST_IS_AFTER_SECOND, MOST_IMPORTANT_FIRST),
                arrayOf(low, important, FIRST_IS_AFTER_SECOND, MOST_IMPORTANT_FIRST),
                arrayOf(basic, low, FIRST_IS_BEFORE_SECOND, MOST_IMPORTANT_FIRST),
                arrayOf(basic, important, FIRST_IS_AFTER_SECOND, MOST_IMPORTANT_FIRST),
                arrayOf(important, low, FIRST_IS_BEFORE_SECOND, MOST_IMPORTANT_FIRST),
                arrayOf(important, basic, FIRST_IS_BEFORE_SECOND, MOST_IMPORTANT_FIRST),
                arrayOf(low, low, EQUAL, LESS_IMPORTANT_FIRST),
                arrayOf(basic, basic, EQUAL, LESS_IMPORTANT_FIRST),
                arrayOf(important, important, EQUAL, LESS_IMPORTANT_FIRST),
                arrayOf(low, basic, FIRST_IS_BEFORE_SECOND, LESS_IMPORTANT_FIRST),
                arrayOf(low, important, FIRST_IS_BEFORE_SECOND, LESS_IMPORTANT_FIRST),
                arrayOf(basic, important, FIRST_IS_BEFORE_SECOND, LESS_IMPORTANT_FIRST),
                arrayOf(important, basic, FIRST_IS_AFTER_SECOND, LESS_IMPORTANT_FIRST),
            )
        }
    }

}

