package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue.EARLIER_FIRST
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue.LATER_FIRST
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue.NO_DEADLINE_SORT
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.EQUAL
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.FIRST_IS_AFTER_SECOND
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.ComparisonResult.FIRST_IS_BEFORE_SECOND
import java.time.ZonedDateTime


@RunWith(Parameterized::class)
class DeadlineComparatorTest(
    private val firstTodo: TodoItemDomainEntity,
    private val secondTodo: TodoItemDomainEntity,
    private val expectedResult: ComparisonResult,
    private val deadlineSortValue: DeadlineSortValue
) {

    @Test
    fun shouldCompareItemsWithDeadlineComparator() {
        deadlineComparatorLauncher.launchTest(
            firstTodo,
            secondTodo,
            expectedResult,
            deadlineSortValue
        )
    }

    companion object {
        private val noDeadline = TodoItemDomainEntity(isDeadlineEnabled = false)
        private val deadlineNow = TodoItemDomainEntity(
            isDeadlineEnabled = true, deadline = ZonedDateTime.now()
        )
        private val deadlineNowPlusWeek = TodoItemDomainEntity(
            isDeadlineEnabled = true,
            deadline = ZonedDateTime.now().plusWeeks(1)
        )

        private val deadlineComparatorLauncher = ComparatorTestLauncher(
            "getDeadlineComparator",
            DeadlineSortValue::class.java
        )

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(noDeadline, noDeadline, EQUAL, NO_DEADLINE_SORT),
                arrayOf(noDeadline, deadlineNow, EQUAL, NO_DEADLINE_SORT),
                arrayOf(deadlineNowPlusWeek, deadlineNow, EQUAL, NO_DEADLINE_SORT),
                arrayOf(noDeadline, deadlineNowPlusWeek, EQUAL, NO_DEADLINE_SORT),
                arrayOf(noDeadline, noDeadline, EQUAL, EARLIER_FIRST),
                arrayOf(noDeadline, deadlineNowPlusWeek, FIRST_IS_AFTER_SECOND, EARLIER_FIRST),
                arrayOf(deadlineNow, deadlineNowPlusWeek, FIRST_IS_BEFORE_SECOND, EARLIER_FIRST),
                arrayOf(noDeadline, noDeadline, EQUAL, LATER_FIRST),
                arrayOf(deadlineNow, deadlineNow, EQUAL, LATER_FIRST),
                arrayOf(noDeadline, deadlineNowPlusWeek, FIRST_IS_AFTER_SECOND, LATER_FIRST),
                arrayOf(deadlineNow, deadlineNowPlusWeek, FIRST_IS_AFTER_SECOND, LATER_FIRST),
            )
        }
    }
}
