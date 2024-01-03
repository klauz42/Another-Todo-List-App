package ru.claus42.anothertodolistapp.data.local.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID


enum class LocalDataItemPriority {
    LOW, BASIC, IMPORTANT
}

@Entity(tableName = "todo_items")
data class TodoItemLocalDataEntity(
    @PrimaryKey val id: UUID,
    val description: String,
    val priority: LocalDataItemPriority,
    val deadline: LocalDateTime,
    @ColumnInfo(name = "is_deadline_enabled") val isDeadlineEnabled: Boolean,
    val done: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "changed_at") val changedAt: LocalDateTime,
    @ColumnInfo(name = "order_index") val orderIndex: Long,
)