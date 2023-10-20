package ru.claus42.anothertodolistapp.presentation.ui.custom.behaviors


import android.content.Context
import android.util.AttributeSet
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
    private var isSubtitleVisible = true
    private var isHide = false

    private var isInitialChildHeightSet = false
    private var isInitialChildHeight = 0
    private var childMaxY: Float = 0f

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
        val maxScroll = (dependency as AppBarLayout).totalScrollRange
        val percentage = abs(dependency.getY()) / maxScroll
        val childPosition = (dependency.getHeight() + dependency.getY()) - child.height

        if (!isInitialChildHeightSet) {
            isInitialChildHeight = child.height
            isInitialChildHeightSet = true

            childMaxY = (dependency.height - isInitialChildHeight).toFloat()
        }

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams

        child.updateViews(percentage)

        child.layoutParams = lp

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