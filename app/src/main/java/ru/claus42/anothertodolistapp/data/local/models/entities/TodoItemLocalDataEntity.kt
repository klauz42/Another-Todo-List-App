package ru.claus42.anothertodolistapp.data.local.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

enum class DataItemPriority {
    LOW, BASIC, IMPORTANT
}

@Entity(tableName = "todo_items")
data class TodoItemLocalDataEntity(
    @PrimaryKey val id: UUID,
    val description: String,
    val priority: DataItemPriority,
    val deadline: LocalDateTime,
    val isDeadlineEnabled: Boolean,
    val done: Boolean,
    val createdAt: LocalDateTime,
    val changedAt: LocalDateTime,
)