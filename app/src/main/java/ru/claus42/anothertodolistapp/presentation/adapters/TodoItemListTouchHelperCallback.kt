package ru.claus42.anothertodolistapp.presentation.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.claus42.anothertodolistapp.R
import kotlin.math.abs
import kotlin.math.max


class TodoItemListTouchHelperCallback(private val adapter: TodoItemListAdapter) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ) {
    interface TodoItemListAdapter {
        fun onDeleteItem(viewHolder: ViewHolder)
        fun onMoveItem(oldPosition: Int, newPosition: Int)
        fun onMoveItemUIUpdate(fromViewHolder: ViewHolder, toViewHolder: ViewHolder)
        fun onChangeItemDoneStatus(viewHolder: ViewHolder)
        fun onChangeItemDoneStatusUIUpdate(viewHolder: ViewHolder)
    }

    private var oldMovingPosition = -1
    private var newMovingPosition = -1

    private var viewHolderToSwipeBack: ViewHolder? = null
    private val needToSwipeBack get() = viewHolderToSwipeBack != null
    private var isDoneThresholdCrossed = false
    private var isDeleteThresholdCrossed = false


    //todo: add haptic feedback
    private fun getVibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private fun Vibrator.simpleVibrate(ms: Long) =
        vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))

    private fun Vibrator.vibrateConfirm() = simpleVibrate(30)
    private fun Vibrator.vibrateReject() = simpleVibrate(15)

    private fun getDeleteDirection(viewHolder: ViewHolder): Int {
        return if (isLTR(viewHolder.itemView))
            ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
    }

    private fun getChangeDoneStatusDirection(viewHolder: ViewHolder): Int {
        return if (isLTR(viewHolder.itemView))
            ItemTouchHelper.RIGHT else ItemTouchHelper.LEFT
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        newMovingPosition = target.adapterPosition
        adapter.onMoveItemUIUpdate(viewHolder, target)

        return false
    }

    private fun isLTR(view: View) =
        (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR)

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        when (direction) {
            getDeleteDirection(viewHolder) -> adapter.onDeleteItem(viewHolder)
            getChangeDoneStatusDirection(viewHolder) -> adapter.onChangeItemDoneStatus(viewHolder)
        }
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                viewHolder?.adapterPosition?.let { oldMovingPosition = it }
            }

            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if (oldMovingPosition != -1 && newMovingPosition != -1
                    && oldMovingPosition != newMovingPosition
                ) {
                    adapter.onMoveItem(oldMovingPosition, newMovingPosition)

                    oldMovingPosition = -1
                    newMovingPosition = -1
                }

                if (isDoneThresholdCrossed) {
                    viewHolderToSwipeBack?.let {
                        val direction = getChangeDoneStatusDirection(it)
                        onSwiped(it, direction)
                    }
                }
            }
        }
    }

    //override to prevent an extra pushing with followed by sticking to the edge of the screen
    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return if (needToSwipeBack) 1.0f else super.getSwipeVelocityThreshold(defaultValue)
    }

    //override to prevent a sticking to the edge of the screen after a full swipe
    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return if (needToSwipeBack) 100.0f else super.getSwipeThreshold(viewHolder)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        isDeleteThresholdCrossed = false
    }

    private fun drawSwipingItemTrack(
        context: Context,
        itemView: View,
        canvas: Canvas,
        dX: Float,
        isSwipeCorrespondingLayoutDirection: Boolean,
        @ColorRes backgroundResourceId: Int,
        @DrawableRes iconResourceId: Int,
        @ColorRes iconTintResourceId: Int = R.color.icon_swipe_tint
    ) {
        val leftBound: Int
        val rightBound: Int
        val topBound: Int = itemView.top
        val bottomBound: Int = itemView.bottom

        val backgroundColor: Int = ContextCompat.getColor(
            context,
            backgroundResourceId
        )
        val background = ColorDrawable(backgroundColor)

        val iconSize: Int =
            context.resources.getDimensionPixelSize(R.dimen.swipe_icon_size)
        val iconColor = ContextCompat.getColor(context, iconTintResourceId)
        val iconLeftBound: Int
        val iconRightBound: Int
        val iconTopBound: Int = with(itemView) { top + (bottom - top - iconSize) / 2 }
        val iconBottomBound: Int = with(itemView) { top + (bottom - top + iconSize) / 2 }
        val icon = ContextCompat.getDrawable(context, iconResourceId)!!.apply {
            DrawableCompat.setTint(this, iconColor)
        }

        //todo: radius corner is using no quiet correctly in this code:
        //      temporary workaround won't work properly with transparent item background
        val itemCornerRadius = context.resources
            .getDimensionPixelSize(R.dimen.item_corner_radius)

        if (isLTR(itemView) && !isSwipeCorrespondingLayoutDirection) {
            rightBound = itemView.right
            leftBound = (itemView.right + dX.toInt() - itemCornerRadius)
                .coerceAtLeast(itemView.left)
            iconRightBound = itemView.right - iconSize
            iconLeftBound = itemView.right - iconSize * 2
        } else {
            rightBound = (itemView.left + dX.toInt() + itemCornerRadius)
                .coerceAtMost(itemView.right)
            leftBound = itemView.left
            iconRightBound = itemView.left + iconSize * 2
            iconLeftBound = itemView.left + iconSize
        }

        background.setBounds(leftBound, topBound, rightBound, bottomBound)
        background.draw(canvas)
        icon.setBounds(iconLeftBound, iconTopBound, iconRightBound, iconBottomBound)
        icon.draw(canvas)
    }

    override fun onChildDraw(
        c: Canvas,
        rV: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val context = rV.context
            val vibrator = getVibrator(context)
            val threshold = context.resources.getDimensionPixelSize(R.dimen.swipe_threshold)
            val itemView = viewHolder.itemView
            val actualDx: Float

            val direction =
                if (dX < 0) ItemTouchHelper.LEFT else if (dX > 0) ItemTouchHelper.RIGHT else -1

            if (direction == getDeleteDirection(viewHolder)) {
                isDoneThresholdCrossed = false
                viewHolderToSwipeBack = null

                actualDx = dX

                drawSwipingItemTrack(
                    context, itemView, c, actualDx, false,
                    R.color.delete_item_swipe_background,
                    R.drawable.delete_swipe
                )

                val deleteThreshold =
                    getSwipeThreshold(viewHolder) * max(itemView.left, itemView.right)

                isDeleteThresholdCrossed = if (abs(actualDx) >= deleteThreshold) {
                    if (!isDeleteThresholdCrossed) vibrator.vibrateConfirm()
                    true
                } else {
                    if (isDeleteThresholdCrossed) vibrator.vibrateReject()
                    false
                }

            } else if (direction == getChangeDoneStatusDirection(viewHolder)) {
                viewHolderToSwipeBack = viewHolder

                actualDx = dX.coerceIn(-threshold.toFloat(), threshold.toFloat())

                drawSwipingItemTrack(
                    context, itemView, c, actualDx, true,
                    R.color.done_item_swipe_background,
                    R.drawable.check
                )

                if (isCurrentlyActive) {
                    isDoneThresholdCrossed = if (abs(dX) >= threshold) {
                        if (!isDoneThresholdCrossed) vibrator.vibrateConfirm()
                        true
                    } else {
                        if (isDoneThresholdCrossed) vibrator.vibrateReject()
                        false
                    }
                }
            } else {
                actualDx = dX
                if (needToSwipeBack && isDoneThresholdCrossed) {
                    viewHolderToSwipeBack?.let { adapter.onChangeItemDoneStatusUIUpdate(it) }
                    viewHolderToSwipeBack = null
                    isDoneThresholdCrossed = false
                }
            }

            super.onChildDraw(c, rV, viewHolder, actualDx, dY, actionState, isCurrentlyActive)
            return
        }

        super.onChildDraw(c, rV, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
