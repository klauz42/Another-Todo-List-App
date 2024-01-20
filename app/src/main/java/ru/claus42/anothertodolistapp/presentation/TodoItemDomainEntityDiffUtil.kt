package ru.claus42.anothertodolistapp.presentation

import androidx.recyclerview.widget.DiffUtil
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity

fun calculateDiff(
    items: List<TodoItemDomainEntity>,
    newItems: List<TodoItemDomainEntity>
) = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
    override fun getOldListSize() = items.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return items[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newItems[newItemPosition]
        val oldItem = items[oldItemPosition]
        return newItem.equalsByContent(oldItem)
    }
}
)