package ru.claus42.anothertodolistapp.presentation.searchtodos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.claus42.anothertodolistapp.databinding.TodoItemSearchResultBinding
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.presentation.calculateDiff
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class SearchResultListAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchResultItemViewHolder>() {

    interface ItemClickListener {
        fun onItemClickListener(id: UUID)
    }

    @Volatile
    var layoutManagerType: SearchLayoutViewType = SearchLayoutViewType.LINEAR

    private var listener: ItemClickListener? = null
    fun setAdapterListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    private val items = mutableListOf<TodoItemDomainEntity>()

    fun submitList(newItems: List<TodoItemDomainEntity>) {
        val result = calculateDiff(items, newItems)

        items.clear()
        items.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemSearchResultBinding.inflate(layoutInflater, parent, false)
        return SearchResultItemViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        val item = items[position]

        listener?.let {
            holder.bind(item, it::onItemClickListener, layoutManagerType)
        }
    }
}