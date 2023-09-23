package ru.claus42.anothertodolistapp.domain.models.entities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

enum class Importance {
    LOW, BASIC, IMPORTANT
}

data class TodoItemDomainEntity(
    val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var importance: Importance = Importance.BASIC,
    var deadline: LocalDateTime? = null,
    var done: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var changedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(
        descriptionValue: String,
        importanceValue: Importance = Importance.BASIC,
        deadlineValue: LocalDateTime? = null,
    ) : this(
            description = descriptionValue,
            importance = importanceValue,
            deadline = deadlineValue,
        )

    private val formatter =  DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    val formattedChangedData: String get() = changedAt.format(formatter)
}