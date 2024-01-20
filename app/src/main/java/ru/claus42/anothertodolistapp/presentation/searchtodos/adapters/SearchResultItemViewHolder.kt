package ru.claus42.anothertodolistapp.presentation.searchtodos.adapters

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.databinding.TodoItemSearchResultBinding
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.util.UUID


class SearchResultItemViewHolder(
    private val binding: TodoItemSearchResultBinding
) : ViewHolder(binding.root) {
    fun bind(
        item: TodoItemDomainEntity,
        itemClickListener: (UUID) -> Unit,
        layoutManagerType: SearchLayoutViewType,
    ) {
        val resources = binding.itemText.context.resources

        binding.todoItem = item

        binding.apply {
            itemText.text = item.description

            itemText.maxLines = when (layoutManagerType) {
                SearchLayoutViewType.LINEAR -> resources.getInteger(R.integer.item_search_layout_linear_max_lines)
                SearchLayoutViewType.GRID -> resources.getInteger(R.integer.item_search_layout_grid_max_lines)
            }

            val backgroundResId = when (item.itemPriority) {
                ItemPriority.LOW -> R.drawable.item_search_result_low_priority_background
                ItemPriority.BASIC -> R.drawable.item_search_result_basic_priority_background
                ItemPriority.IMPORTANT -> R.drawable.item_search_result_important_priority_background
            }
            itemText.setBackgroundResource(backgroundResId)
        }

        itemView.setOnClickListener {
            itemClickListener(item.id)
        }
    }
}