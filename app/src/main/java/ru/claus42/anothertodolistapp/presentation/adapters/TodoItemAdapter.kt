package ru.claus42.anothertodolistapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.claus42.anothertodolistapp.databinding.TodoItemBinding
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TodoItemAdapter() :
    RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

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
        holder.bind(item)
    }

    class TodoItemViewHolder(private val binding: TodoItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoItemDomainEntity) {
            binding.todoItem = item

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

                executePendingBindings()
            }
        }
    }

    object TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItemDomainEntity>() {
        override fun areItemsTheSame(oldItem: TodoItemDomainEntity, newItem: TodoItemDomainEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItemDomainEntity, newItem: TodoItemDomainEntity) =
            oldItem == newItem
    }
}