package ru.claus42.anothertodolistapp.presentation.ui.custom.behaviors


import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.presentation.ui.custom.views.ItemListHeaderLayout
import kotlin.math.abs


class ItemListHeaderBehavior(context: Context?, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<ItemListHeaderLayout>(context, attrs) {

    private val expandedTitleMarginStart: Int
    private val collapsedTitleMarginStart: Int
    private val expandedTitleMarginBottom: Int
    private val collapsedTitleMarginBottom: Int
    private var expandedTitleTextSize: Int
    private var collapsedTitleTextSize: Int
    private var isSubtitleVisible = true
    private var isHide = false

    private var isInitialChildHeightSet = false
    private var isInitialChildHeight = 0
    
    init {
        val styledAttrs = context!!.obtainStyledAttributes(attrs, R.styleable.ItemListHeaderBehaviorAttrs)
        try {
            expandedTitleTextSize = styledAttrs.getDimensionPixelSize(
                R.styleable.ItemListHeaderBehaviorAttrs_expandedTitleTextSize,
                context.resources.getDimensionPixelSize(R.dimen.item_list_header_expanded_title_text_size)
            )
            collapsedTitleTextSize = styledAttrs.getDimensionPixelSize(
                R.styleable.ItemListHeaderBehaviorAttrs_collapsedTitleTextSize,
                context.resources.getDimensionPixelSize(R.dimen.item_list_header_collapsed_title_text_size)
            )

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

    companion object {
        fun getToolbarHeight(context: Context): Int {
            var result = 0
            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                result = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    context.resources.displayMetrics
                )
            }
            return result
        }

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


            setTitleTextSize(
                getTranslationOffset(
                    expandedTitleTextSize,
                    collapsedTitleTextSize,
                    layoutPercentage
                )
            )

            val subtitleDisappearancePercent = 0.4f

            setSubtitleAlpha(1f - 1/subtitleDisappearancePercent * layoutPercentage)

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
        val maxScroll = (dependency as AppBarLayout).totalScrollRange
        val percentage = abs(dependency.getY()) / maxScroll
        val childPosition = (dependency.getHeight() + dependency.getY()) - child.height
        
        if (!isInitialChildHeightSet) {
            isInitialChildHeight = child.height
            isInitialChildHeightSet = true
        }

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams

        child.updateViews(percentage)

        child.layoutParams = lp

        val childMaxY = (dependency.height - isInitialChildHeight).toFloat()
        child.y = if (childPosition > childMaxY) childMaxY else childPosition

        if (isHide && percentage < 1) {
            child.visibility = View.VISIBLE
            isHide = false
        } else if (!isHide && percentage > 1f) {
            child.visibility = View.GONE
            isHide = true
        }
        return true
    }

}