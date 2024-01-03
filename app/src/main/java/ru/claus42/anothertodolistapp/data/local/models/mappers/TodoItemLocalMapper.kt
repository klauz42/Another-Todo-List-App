package ru.claus42.anothertodolistapp.data.local.models.mappers

import ru.claus42.anothertodolistapp.data.local.models.entities.LocalDataItemPriority
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity


private fun LocalDataItemPriority.toPriority(): ItemPriority {
    return when (this) {
        LocalDataItemPriority.LOW -> ItemPriority.LOW
        LocalDataItemPriority.BASIC -> ItemPriority.BASIC
        LocalDataItemPriority.IMPORTANT -> ItemPriority.IMPORTANT
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
        changedAt = this.changedAt,
    )
}


private fun ItemPriority.toLocalDataPriority(): LocalDataItemPriority {
    return when (this) {
        ItemPriority.LOW -> LocalDataItemPriority.LOW
        ItemPriority.BASIC -> LocalDataItemPriority.BASIC
        ItemPriority.IMPORTANT -> LocalDataItemPriority.IMPORTANT
    }
}


const val ORDER_NO_SET = -1L

fun TodoItemDomainEntity.toLocalDataModel(): TodoItemLocalDataEntity {
    return TodoItemLocalDataEntity(
        id = this.id,
        description = this.description,
        priority = this.itemPriority.toLocalDataPriority(),
        deadline = this.deadline,
        isDeadlineEnabled = this.isDeadlineEnabled,
        done = this.done,
        createdAt = this.createdAt,
        changedAt = this.changedAt,
        orderIndex = ORDER_NO_SET,
    )
}