package ru.claus42.anothertodolistapp.data.local.models.mappers

import ru.claus42.anothertodolistapp.data.local.models.entities.DataItemPriority
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity

fun DataItemPriority.toPriority(): ItemPriority {
    return when (this) {
        DataItemPriority.LOW -> ItemPriority.LOW
        DataItemPriority.BASIC -> ItemPriority.BASIC
        DataItemPriority.IMPORTANT -> ItemPriority.IMPORTANT
    }
}

fun TodoItemLocalDataEntity.toDomainModel(): TodoItemDomainEntity {
    return TodoItemDomainEntity(
        id = this.id,
        description = this.description,
        itemPriority = this.priority.toPriority(),
        deadline = this.deadline,
        isDeadlineEnabled = this.isDeadlineEnabled,
        done = this.done,
        createdAt = this.createdAt,
        changedAt = this.changedAt
    )
}


fun ItemPriority.toDataPriority(): DataItemPriority {
    return when (this) {
        ItemPriority.LOW -> DataItemPriority.LOW
        ItemPriority.BASIC -> DataItemPriority.BASIC
        ItemPriority.IMPORTANT -> DataItemPriority.IMPORTANT
    }
}

fun TodoItemDomainEntity.toLocalDataModel(): TodoItemLocalDataEntity {
    return TodoItemLocalDataEntity(
        id = this.id,
        description = this.description,
        priority = this.itemPriority.toDataPriority(),
        deadline = this.deadline,
        isDeadlineEnabled = this.isDeadlineEnabled,
        done = this.done,
        createdAt = this.createdAt,
        changedAt = this.changedAt
    )
}