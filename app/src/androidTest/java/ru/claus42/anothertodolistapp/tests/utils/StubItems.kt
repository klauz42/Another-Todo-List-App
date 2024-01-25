package ru.claus42.anothertodolistapp.tests.utils

import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.time.ZonedDateTime

fun generateTodoItems(size: Int = 30): List<TodoItemDomainEntity> {
    val descriptions = listOf(
        "Lorem ipsum dolor sit amet", "consectetur adipiscing elit", "sed do eiusmod tempor incididunt",
        "ut labore et dolore magna aliqua", "Ut enim ad minim veniam", "quis nostrud exercitation ullamco",
        "laboris nisi ut aliquip ex ea commodo", "Duis aute irure dolor in reprehenderit", "in voluptate velit esse",
        "cillum dolore eu fugiat nulla pariatur", "Excepteur sint occaecat cupidatat", "non proident, sunt in culpa",
        "qui officia deserunt mollit anim id est laborum", "Sed ut perspiciatis unde omnis iste natus",
        "error sit voluptatem accusantium doloremque", "laudantium, totam rem aperiam", "eaque ipsa quae ab illo inventore",
        "veritatis et quasi architecto beatae vitae", "dicta sunt explicabo", "Nemo enim ipsam voluptatem",
        "quia voluptas sit aspernatur aut odit aut fugit", "sed quia consequuntur magni dolores eos",
        "qui ratione voluptatem sequi nesciunt", "Neque porro quisquam est", "qui dolorem ipsum quia dolor sit amet",
        "consectetur, adipisci velit", "sed quia non numquam eius modi tempora", "incidunt ut labore et dolore",
        "magnam aliquam quaerat voluptatem", "Ut enim ad minima veniam", "quis nostrum exercitationem ullam"
    )

    return List(size) { i ->
        TodoItemDomainEntity(
            description = descriptions[i % descriptions.size],
            itemPriority = ItemPriority.entries[i % ItemPriority.entries.size],
            deadline = ZonedDateTime.now().plusDays((i + 1) * 2L),
            isDeadlineEnabled = i % 2 == 0,
            done = i % 3 == 0,
            createdAt = ZonedDateTime.now().minusDays(i * 2L),
            changedAt = ZonedDateTime.now().minusDays((i + 1) * 2L)
        )
    }
}