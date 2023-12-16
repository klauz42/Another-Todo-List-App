package ru.claus42.anothertodolistapp.presentation.todoitemlist.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.presentation.todoitemlist.ui.SavedState

const val VISIBILITY_KEY = "visibility"

class ItemListHeaderLayout(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private val title: TextView by lazy { findViewById(R.id.header_title) }
    private val subtitle: TextView? by lazy { findViewById(R.id.header_subtitle) }
    private val expandedTitleTextSize: Float
    private val collapsedTitleTextSize: Float

    private val titleTextSizeAnimator: ValueAnimator

    init {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ItemListHeaderLayout)
        try {
            expandedTitleTextSize = styledAttrs.getDimensionPixelSize(
                R.styleable.ItemListHeaderLayout_expandedTitleSize,
                context.resources.getDimensionPixelSize(R.dimen.item_list_header_expanded_title_text_size)
            ).toFloat()
            collapsedTitleTextSize = styledAttrs.getDimensionPixelSize(
                R.styleable.ItemListHeaderLayout_collapsedTitleSize,
                context.resources.getDimensionPixelSize(R.dimen.item_list_header_collapsed_title_text_size)
            ).toFloat()
        } finally {
            styledAttrs.recycle()
        }

        titleTextSizeAnimator = ValueAnimator.ofFloat(
            expandedTitleTextSize,
            collapsedTitleTextSize
        ).apply {
            addUpdateListener { animator ->
                val newSize = animator.animatedValue as Float
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize)
            }
        }

    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.data.putInt(VISIBILITY_KEY, this.visibility)
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) {
            this.visibility = state.data.getInt(VISIBILITY_KEY)
        }
    }


    private val titleLayoutParams: MarginLayoutParams
        get() = title.layoutParams as MarginLayoutParams

    fun setTitleTextSizeAnimatorPlaytime(scrollRatio: Float) {
        titleTextSizeAnimator.currentPlayTime =
            (scrollRatio * titleTextSizeAnimator.duration).toLong()
    }

    fun showSubtitle() {
        subtitle?.visibility = VISIBLE
    }

    fun hideSubtitle() {
        subtitle?.visibility = GONE
    }

    fun setSubtitleAlpha(alpha: Float) {
        subtitle?.alpha = alpha
    }

    fun setSubtitleText(text: String) {
        subtitle?.text = text
    }

    fun setTitleMarginStart(marginPx: Int) {
        titleLayoutParams.marginStart = marginPx
    }

    fun setTitleMarginBottom(marginPx: Int) {
        titleLayoutParams.bottomMargin = marginPx
    }


    fun setTitleTextSize(size: Float) {
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

}