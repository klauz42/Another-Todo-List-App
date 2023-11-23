package ru.claus42.anothertodolistapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.databinding.TodoItemBinding
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.views.callbacks.ItemListTouchHelperCallback
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID

interface DoneCheckBoxListener {
    fun onChecked(checkBoxView: CompoundButton, id: UUID, isDone: Boolean)
}

class TodoItemAdapter(
    private val itemClickListener: (UUID) -> Unit,
    private val doneCheckBoxListener: (UUID, Boolean) -> Unit,
    private val moveItemListener: (from: Int, to: Int) -> Unit
) : RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>(),
    ItemListTouchHelperCallback.ItemListAdapter {

    private val differ = AsyncListDiffer(this, TodoItemDiffCallback)

    fun submitList(newItems: List<TodoItemDomainEntity>) {
        differ.submitList(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item, itemClickListener, doneCheckBoxListener)

        updateItemBackground(holder, position)
    }

    private fun updateItemBackground(holder: ViewHolder, position: Int) {
        val backgroundResId = when {
            itemCount == 1 -> R.drawable.item_shape_only
            position == 0 -> R.drawable.item_shape_top
            position == itemCount - 1 -> R.drawable.item_shape_bottom
            else -> R.drawable.item_shape_default
        }
        (holder as TodoItemViewHolder).itemContainer.setBackgroundResource(backgroundResId)
    }

    override fun onItemMove(fromViewHolder: ViewHolder, toViewHolder: ViewHolder) {
        val fromPosition = fromViewHolder.adapterPosition
        val toPosition = toViewHolder.adapterPosition

        moveItemListener(fromPosition, toPosition)

        updateItemBackground(fromViewHolder, toPosition)
        updateItemBackground(toViewHolder, fromPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    class TodoItemViewHolder(private val binding: TodoItemBinding) : ViewHolder(binding.root) {
        val itemContainer = binding.itemContainer

        fun bind(
            item: TodoItemDomainEntity,
            itemClickListener: (UUID) -> Unit,
            doneCheckBoxListener: (UUID, Boolean) -> Unit
        ) {
            binding.todoItem = item
            binding.doneCheckBoxListener = object : DoneCheckBoxListener {
                override fun onChecked(checkBoxView: CompoundButton, id: UUID, isDone: Boolean) {
                    if (checkBoxView.isPressed)
                        doneCheckBoxListener(id, isDone)
                }
            }

            binding.apply {
                doneCheckbox.isChecked = item.done
                itemDescription.text = item.description
                val deadline = item.deadline
                if (deadline != null) {
                    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                    itemDeadline.text = deadline.format(formatter)
                    itemDeadline.isVisible = true
                } else {
                    itemDeadline.text = null
                    itemDeadline.isVisible = false
                }
            }

            itemView.setOnClickListener { itemClickListener(item.id) }
        }
    }

    object TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItemDomainEntity>() {
        override fun areItemsTheSame(oldItem: TodoItemDomainEntity, newItem: TodoItemDomainEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TodoItemDomainEntity,
            newItem: TodoItemDomainEntity
        ) =
            oldItem == newItem
    }
}
