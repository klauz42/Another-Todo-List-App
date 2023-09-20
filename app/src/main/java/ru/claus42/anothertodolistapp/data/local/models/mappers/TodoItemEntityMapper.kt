package ru.claus42.anothertodolistapp.data.local.models.mappers

import ru.claus42.anothertodolistapp.data.local.models.entities.DataImportance
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalEntity
import ru.claus42.anothertodolistapp.domain.models.entities.Importance
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemEntity

fun DataImportance.toImportance() : Importance {
    return when(this) {
        DataImportance.LOW -> Importance.LOW
        DataImportance.BASIC -> Importance.BASIC
        DataImportance.IMPORTANT -> Importance.IMPORTANT
    }
}

fun TodoItemLocalEntity.toDomainModel() : TodoItemEntity {
    return TodoItemEntity(
        id = this.id,
        description = this.description,
        importance = this.importance.toImportance(),
        deadline = this.deadline,
        done = this.done,
        createdAt = this.createdAt,
        changedAt = this.changedAt
    )
}


fun Importance.toDataImportance(): DataImportance {
    return when(this) {
        Importance.LOW -> DataImportance.LOW
        Importance.BASIC -> DataImportance.BASIC
        Importance.IMPORTANT -> DataImportance.IMPORTANT
    }
}

fun TodoItemEntity.toDataLocalModel() : TodoItemLocalEntity {
    return TodoItemLocalEntity(
        id = this.id,
        description = this.description,
        importance = this.importance.toDataImportance(),
        deadline = this.deadline,
        done = this.done,
        createdAt = this.createdAt,
        changedAt = this.changedAt
    )
}