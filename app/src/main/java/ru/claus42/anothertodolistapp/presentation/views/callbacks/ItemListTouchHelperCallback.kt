package ru.claus42.anothertodolistapp.presentation.views.callbacks

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemListTouchHelperCallback(
    private val adapter: ItemListAdapter
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder, target)

        return true
    }

    //todo: temporary solution to prevent calling of unimplemented swipe actions
    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return 0
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    interface ItemListAdapter {
        fun onItemMove(
            fromViewHolder: RecyclerView.ViewHolder,
            toViewHolder: RecyclerView.ViewHolder
        )
    }
}