package ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.databinding.TodoItemBinding
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.calculateDiff
import ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.itemtouchhelper.TodoItemListTouchHelperCallback
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Collections
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class TodoItemListAdapter @Inject constructor() :
    RecyclerView.Adapter<TodoItemListAdapter.TodoItemViewHolder>(),
    TodoItemListTouchHelperCallback.AdapterListener {
    interface DoneCheckBoxListener {
        fun onChecked(checkBoxView: CompoundButton, id: UUID, isDone: Boolean)
    }

    interface Listener {
        fun itemClickListener(id: UUID)
        fun doneCheckBoxListener(id: UUID, isDone: Boolean)
        fun moveItemListener(fromId: UUID, toId: UUID)
        fun deleteItemListener(item: TodoItemDomainEntity)
        fun undoItemDeletionListener(onDeletionCallback: () -> Unit)
    }

    private var listener: Listener? = null
    fun setAdapterListener(listener: Listener?) {
        this.listener = listener
    }

    private val items = mutableListOf<TodoItemDomainEntity>()

    private var shouldUpdateOldLastBackground = false
    private var oldLastInNewListPosition = 0

    private val edgeItemsUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            if (position == 0) {
                notifyItemChanged(count)
            } else if (shouldUpdateOldLastBackground) {
                notifyItemChanged(oldLastInNewListPosition)
            }
        }

        override fun onRemoved(position: Int, count: Int) {
            if (position == 0 && itemCount > 0) notifyItemChanged(0)
            if (position >= itemCount) {
                notifyItemChanged(itemCount - 1)
            }
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    fun submitList(newItems: List<TodoItemDomainEntity>) {
        val result = calculateDiff(items, newItems)

        if (itemCount > 0 && newItems.isNotEmpty()) {
            val oldLast = itemCount - 1
            val newLastInOldLostPosition = result.convertNewPositionToOld(newItems.size - 1)

            if (newLastInOldLostPosition == oldLast) {
                shouldUpdateOldLastBackground = false
            } else {
                shouldUpdateOldLastBackground = true
                oldLastInNewListPosition = result.convertOldPositionToNew(oldLast)
            }
        }

        items.clear()
        items.addAll(newItems)
        result.dispatchUpdatesTo(this)
        result.dispatchUpdatesTo(edgeItemsUpdateCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = items[position]

        listener?.let {
            holder.bind(item, it::itemClickListener, it::doneCheckBoxListener)
        }

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

    private fun updateEdgeElementsBackgroundAfterDeletion(position: Int) {
        if (itemCount != 0) {
            if (position == 0) {
                notifyItemChanged(0)
            } else if (position == itemCount - 1) {
                notifyItemChanged(itemCount - 2)
            }
        }
    }

    private fun updateEdgeElementsBackgroundAfterRestoration(position: Int) {
        if (position == 0 && itemCount > 0) {
            notifyItemChanged(1)
        } else if (position == itemCount - 1) {
            notifyItemChanged(itemCount - 2)
        }
    }

    private fun showUndoDeletionSnackBar(position: Int, item: TodoItemDomainEntity) {
        listener?.undoItemDeletionListener {
            items.add(position, item)
            notifyItemInserted(position)
            updateEdgeElementsBackgroundAfterRestoration(position)
        }
    }

    override fun onDeleteItem(viewHolder: ViewHolder) {
        val position = viewHolder.bindingAdapterPosition
        notifyItemRemoved(position)
        updateEdgeElementsBackgroundAfterDeletion(position)

        val deletingItem = items.removeAt(position)
        listener?.deleteItemListener(deletingItem)

        showUndoDeletionSnackBar(position, deletingItem)
    }

    override fun onMoveItem(oldPosition: Int, newPosition: Int) {
        val shift = if (oldPosition < newPosition) -1 else if (oldPosition > newPosition) +1 else 0

        val moveFromId = items[newPosition].id
        val moveToId = items[newPosition + shift].id

        if (moveFromId != moveToId)
            listener?.moveItemListener(moveFromId, moveToId)
    }

    override fun onMoveItemUIUpdate(fromViewHolder: ViewHolder, toViewHolder: ViewHolder) {
        val fromPosition = fromViewHolder.bindingAdapterPosition
        val toPosition = toViewHolder.bindingAdapterPosition

        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)

        updateItemBackground(fromViewHolder, toPosition)
        updateItemBackground(toViewHolder, fromPosition)
    }

    override fun onChangeItemDoneStatus(viewHolder: ViewHolder) {
        val position = viewHolder.bindingAdapterPosition
        val id = items[position].id
        val newDoneStatus = !items[position].done

        val newItems = items.map {
            if (it.id == id) {
                it.copy(
                    done = newDoneStatus,
                )
            } else {
                it.copy()
            }
        }
        items.clear()
        items.addAll(newItems)

        listener?.doneCheckBoxListener(id, newDoneStatus)
    }

    override fun onChangeItemDoneStatusUIUpdate(viewHolder: ViewHolder) {
        val position = viewHolder.bindingAdapterPosition
        notifyItemChanged(position)
    }

    class TodoItemViewHolder(private val binding: TodoItemBinding) : ViewHolder(binding.root) {
        val itemContainer = binding.itemContainer

        fun bind(
            item: TodoItemDomainEntity,
            itemClickListener: (UUID) -> Unit,
            doneCheckBoxListener: (UUID, Boolean) -> Unit,
        ) {
            binding.todoItem = item
            binding.doneCheckBoxListener = object : DoneCheckBoxListener {
                override fun onChecked(
                    checkBoxView: CompoundButton,
                    id: UUID,
                    isDone: Boolean
                ) {
                    if (checkBoxView.isPressed)
                        doneCheckBoxListener(item.id, isDone)
                }
            }

            binding.apply {
                doneCheckbox.isChecked = item.done
                itemDescription.text = item.description
                val deadline = item.deadline
                if (item.isDeadlineEnabled) {
                    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    itemDeadline.text = deadline.format(formatter)
                    itemDeadline.isVisible = true
                } else {
                    itemDeadline.isVisible = false
                }
                infoIcon.setOnClickListener {
                    itemClickListener(item.id)
                }
            }
        }
    }
}
