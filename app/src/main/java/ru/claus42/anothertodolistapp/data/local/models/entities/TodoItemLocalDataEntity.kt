package ru.claus42.anothertodolistapp.data.local.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

enum class DataImportance {
    LOW, BASIC, IMPORTANT
}

@Entity(tableName = "todo_items")
data class TodoItemLocalDataEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var description: String = "",
    var importance: DataImportance = DataImportance.BASIC,
    var deadline: LocalDateTime? = null,
    var done: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var changedAt: LocalDateTime = LocalDateTime.now()
)