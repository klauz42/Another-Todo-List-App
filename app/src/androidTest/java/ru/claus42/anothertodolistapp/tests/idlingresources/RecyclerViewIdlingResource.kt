package ru.claus42.anothertodolistapp.tests.idlingresources

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingResource


class RecyclerViewIdlingResource(
    private val recyclerView: RecyclerView,
    expectedItemCount: Int,
) : IdlingResource {

    @Volatile
    var itemCount = expectedItemCount
        private set

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private val isIdle get() = (recyclerView.adapter?.itemCount ?: 0) >= itemCount

    init {
        recyclerView
            .adapter
            ?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    checkIdleState()
                }
            }
        )
    }

    fun decrementItemCount() {
        itemCount--
    }

    private fun checkIdleState() {
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
    }

    override fun getName(): String = RecyclerViewIdlingResource::class.java.simpleName

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    override fun isIdleNow(): Boolean {
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }
}