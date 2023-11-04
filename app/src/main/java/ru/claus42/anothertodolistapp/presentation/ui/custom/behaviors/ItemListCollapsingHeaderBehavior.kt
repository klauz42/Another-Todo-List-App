package ru.claus42.anothertodolistapp.presentation.ui.custom.behaviors


import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.presentation.ui.custom.SavedState
import ru.claus42.anothertodolistapp.presentation.ui.custom.views.ItemListHeaderLayout
import kotlin.math.abs

const val LAST_CHILD_HEIGHT_KEY = "lastChildHeight"
const val IS_CHILD_MAX_Y_SET_KEY = "isChildMaxYSet"
const val CHILD_MAX_Y_KEY = "childMaxY"

class ItemListCollapsingHeaderBehavior(context: Context?, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<ItemListHeaderLayout>(context, attrs) {

    private val expandedTitleMarginStart: Int
    private val collapsedTitleMarginStart: Int
    private val expandedTitleMarginBottom: Int
    private val collapsedTitleMarginBottom: Int
    private var isSubtitleVisible = true

    private var isChildMaxYSet = false
    private var childMaxY: Float = 0f

    private var lastChildHeight: Int = 0

    init {
        val styledAttrs =
            context!!.obtainStyledAttributes(attrs, R.styleable.ItemListHeaderBehaviorAttrs)
        try {
            expandedTitleMarginStart = styledAttrs.getDimensionPixelOffset(
                R.styleable.ItemListHeaderBehaviorAttrs_expandedTitleMarginStart,
                context.resources.getDimensionPixelSize(R.dimen.item_list_header_expanded_title_margin_start)
            )
            collapsedTitleMarginStart = styledAttrs.getDimensionPixelOffset(
                R.styleable.ItemListHeaderBehaviorAttrs_collapsedTitleMarginStart,
                context.resources.getDimensionPixelSize(R.dimen.item_list_header_collapsed_title_margin_start)
            )

            expandedTitleMarginBottom = context.resources.getDimensionPixelSize(
                R.dimen.item_list_header_expanded_title_margin_bottom
            )
            collapsedTitleMarginBottom = context.resources.getDimensionPixelSize(
                R.dimen.item_list_header_collapsed_title_margin_bottom
            )
        } finally {
            styledAttrs.recycle()
        }
    }

    override fun onSaveInstanceState(
        parent: CoordinatorLayout,
        child: ItemListHeaderLayout
    ): Parcelable {
        val superState = super.onSaveInstanceState(parent, child)
        val savedState = SavedState(superState)
        savedState.data.putInt(LAST_CHILD_HEIGHT_KEY, child.height)
        savedState.data.putBoolean(IS_CHILD_MAX_Y_SET_KEY, isChildMaxYSet)
        savedState.data.putFloat(CHILD_MAX_Y_KEY, childMaxY)
        return savedState
    }

    override fun onRestoreInstanceState(
        parent: CoordinatorLayout,
        child: ItemListHeaderLayout,
        state: Parcelable
    ) {
        if (state is SavedState) {
            super.onRestoreInstanceState(parent, child, state.superState)
            lastChildHeight = state.data.getInt(LAST_CHILD_HEIGHT_KEY)
            isChildMaxYSet = state.data.getBoolean(IS_CHILD_MAX_Y_SET_KEY)
            childMaxY = state.data.getFloat(CHILD_MAX_Y_KEY)
        } else {
            super.onRestoreInstanceState(parent, child, state)
        }
    }


    companion object {
        private fun getTranslationOffset(
            expandedOffset: Int,
            collapsedOffset: Int,
            ratio: Float
        ): Float {
            return expandedOffset + ratio * (collapsedOffset - expandedOffset)
        }
    }

    private fun ItemListHeaderLayout.updateViews(layoutPercentage: Float) {
        this.run {
            setTitleMarginStart(
                getTranslationOffset(
                    expandedTitleMarginStart,
                    collapsedTitleMarginStart,
                    layoutPercentage
                ).toInt()
            )

            setTitleMarginBottom(
                getTranslationOffset(
                    expandedTitleMarginBottom,
                    collapsedTitleMarginBottom,
                    layoutPercentage
                ).toInt()
            )

            setTitleTextSizeAnimatorPlaytime(layoutPercentage)

            val subtitleDisappearancePercent = 0.4f

            setSubtitleAlpha(1f - 1 / subtitleDisappearancePercent * layoutPercentage)

            if (isSubtitleVisible && layoutPercentage >= subtitleDisappearancePercent) {
                hideSubtitle()
                isSubtitleVisible = !isSubtitleVisible
            } else if (!isSubtitleVisible && layoutPercentage < subtitleDisappearancePercent) {
                showSubtitle()
                isSubtitleVisible = !isSubtitleVisible
            }
        }
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ItemListHeaderLayout,
        dependency: View
    ) = dependency is AppBarLayout

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ItemListHeaderLayout,
        dependency: View
    ): Boolean {
        val childHeight =
            lastChildHeight.takeIf { it != 0 }.also { lastChildHeight = 0 } ?: child.height

        if (!isChildMaxYSet) {
            childMaxY = (dependency.height - childHeight).toFloat()
            isChildMaxYSet = true
        }

        val maxScroll = (dependency as AppBarLayout).totalScrollRange
        val percentage = abs(dependency.y) / maxScroll
        val childPosition = (dependency.getHeight() + dependency.y) - childHeight
        val lp = child.layoutParams as CoordinatorLayout.LayoutParams

        child.updateViews(percentage)
        child.layoutParams = lp
        child.y = if (childPosition > childMaxY) childMaxY else childPosition

        return true
    }
}