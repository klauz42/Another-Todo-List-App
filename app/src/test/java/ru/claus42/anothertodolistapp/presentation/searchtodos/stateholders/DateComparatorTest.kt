package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType.CHANGED
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType.CREATION
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue.NEW_ONES_FIRST
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue.OLD_ONES_FIRST
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.EQUAL
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.FIRST_IS_AFTER_SECOND
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.FIRST_IS_BEFORE_SECOND
import java.time.ZonedDateTime


@RunWith(Parameterized::class)
class DateComparatorTest(
    private val firstTodo: TodoItemDomainEntity,
    private val secondTodo: TodoItemDomainEntity,
    private val expectedResult: ComparisonResult,
    private val dateSortType: DateSortType,
    private val dateSortValue: DateSortValue,
) {

    @Test
    fun shouldCompareItemsWithDateComparator() {
        dateComparatorLauncher.launchTest(
            firstTodo,
            secondTodo,
            expectedResult,
            dateSortType,
            dateSortValue,
        )
    }

    companion object {
        private val now = ZonedDateTime.now()
        private val creationNow_changedNowPlusThreeWeeks =
            TodoItemDomainEntity(createdAt = now, changedAt = now.plusWeeks(3))
        private val creationNowPlusWeek_changedPlusTwoWeeks =
            TodoItemDomainEntity(createdAt = now.plusWeeks(1), changedAt = now.plusWeeks(2))
        private val creationNow_changedNowPlusTwoWeeks =
            TodoItemDomainEntity(createdAt = now, changedAt = now.plusWeeks(2))
        private val creationNowPlusWeek_changedNowPlusThreeWeeks =
            TodoItemDomainEntity(createdAt = now.plusWeeks(1), changedAt = now.plusWeeks(3))

        private val dateComparatorLauncher = ComparatorTestLauncher(
            "getDateComparator",
            DateSortType::class.java,
            DateSortValue::class.java
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNowPlusWeek_changedPlusTwoWeeks, FIRST_IS_AFTER_SECOND, CREATION, NEW_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNow_changedNowPlusTwoWeeks, EQUAL, CREATION, NEW_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNowPlusWeek_changedPlusTwoWeeks, FIRST_IS_BEFORE_SECOND, CREATION, OLD_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNow_changedNowPlusTwoWeeks, EQUAL, CREATION, OLD_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNowPlusWeek_changedNowPlusThreeWeeks, EQUAL, CHANGED, NEW_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNow_changedNowPlusTwoWeeks, FIRST_IS_BEFORE_SECOND, CHANGED, NEW_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNowPlusWeek_changedNowPlusThreeWeeks, EQUAL, CHANGED, OLD_ONES_FIRST),
                arrayOf(creationNow_changedNowPlusThreeWeeks, creationNow_changedNowPlusTwoWeeks, FIRST_IS_AFTER_SECOND, CHANGED, OLD_ONES_FIRST),
            )
        }
    }
}

