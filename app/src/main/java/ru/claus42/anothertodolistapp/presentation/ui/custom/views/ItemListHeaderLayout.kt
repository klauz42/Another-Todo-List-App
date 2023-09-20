package ru.claus42.anothertodolistapp.presentation.ui.custom.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.claus42.anothertodolistapp.R


class ItemListHeaderLayout(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private val title: TextView by lazy { findViewById(R.id.header_title) }
    private val subtitle: TextView? by lazy { findViewById(R.id.header_subtitle) }
    private val imageButton: ImageButton by lazy { findViewById(R.id.show_hide_done_button) }

    private val titleLayoutParams: MarginLayoutParams
        get() = title.layoutParams as MarginLayoutParams

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